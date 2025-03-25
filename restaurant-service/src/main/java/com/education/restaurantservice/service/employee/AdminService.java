package com.education.restaurantservice.service.employee;

import com.education.restaurantservice.dto.employee.*;
import com.education.restaurantservice.entity.employee.Admin;
import com.education.restaurantservice.entity.employee.Waiter;
import com.education.restaurantservice.exception.employee.EmployeeIsDeletedException;
import com.education.restaurantservice.exception.employee.EmployeeNotFoundException;
import com.education.restaurantservice.repository.employee.AdminRepository;
import com.education.restaurantservice.repository.employee.WaiterRepository;
import com.education.restaurantservice.util.EmployeeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final WaiterRepository waiterRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<WaiterDTO> getAllWaiters() {
        return waiterRepository.findAll().stream()
                .map(EmployeeUtils::convertWaiterToWaiterDTO)
                .toList();
    }

    public WaiterDTO getWaiterById(Long id) {
        Waiter waiter = waiterRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Waiter with id: " + id + " not found"));
        return EmployeeUtils.convertWaiterToWaiterDTO(waiter);
    }

//    public List<OrderDTO> getWaiterOrders(Long id) {
//        WaiterDTO waiterDTO = getWaiterById(id);
//        return waiterDTO.getOrders();
//    }

    public WaiterDTO createWaiter(WaiterRegistrationDTO registrationDTO) {
        EmployeeUtils.validatePhoneNumber(registrationDTO.getPhoneNumber());
        EmployeeUtils.checkPhoneNumberExists(registrationDTO.getPhoneNumber(), waiterRepository, adminRepository);

        Waiter waiter = new Waiter();
        waiter.setPhoneNumber(registrationDTO.getPhoneNumber());
        waiter.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        waiter.setFirstName(registrationDTO.getFirstName());
        waiter.setLastName(registrationDTO.getLastName());
        Waiter savedWaiter = waiterRepository.save(waiter);
        return EmployeeUtils.convertWaiterToWaiterDTO(savedWaiter);
    }

    public void changeWaiterPassword(Long id, ChangePasswordRequestDTO request) {
        Waiter waiter = waiterRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("User with id: " + id + " not found"));

        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        EmployeeUtils.checkPasswords(oldPassword, newPassword, waiter.getPassword(), passwordEncoder);
        waiter.setPassword(passwordEncoder.encode(newPassword));
        waiterRepository.save(waiter);
    }

    public WaiterDTO updateWaiter(Long id, ChangeEmployeeDataRequestDTO changeEmployeeDataRequestDTO) {
        Waiter waiter = waiterRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Waiter with id: " + id + " not found"));

        EmployeeUtils.updateEmployeeFields(waiter, changeEmployeeDataRequestDTO, waiterRepository, adminRepository);
        Waiter updatedWaiter = waiterRepository.save(waiter);
        return EmployeeUtils.convertWaiterToWaiterDTO(updatedWaiter);
    }

    public void deleteWaiter(Long id) {
        Waiter waiter = getWaiterWithIdAndIsDeletedCheck(id);
        waiter.setDeleted(true);
        waiterRepository.save(waiter);
    }

    public WaiterDTO restoreWaiter(Long id) {
        Waiter waiter = waiterRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Waiter with id: " + id + " not found"));
        if (!waiter.isDeleted()) {
            throw new IllegalArgumentException("Waiter is not deleted");
        }
        waiter.setDeleted(false);
        waiterRepository.save(waiter);
        return EmployeeUtils.convertWaiterToWaiterDTO(waiter);
    }

    public Admin getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("Current user not found in security context");
        }

        String phoneNumber = ((UserDetails) authentication.getPrincipal()).getUsername();
        return adminRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->  new EmployeeNotFoundException(
                        "User with phone number: " + phoneNumber + " is not found"));
    }

    public void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) {
        String oldPassword = changePasswordRequestDTO.getOldPassword();
        String newPassword = changePasswordRequestDTO.getNewPassword();
        Admin currentAdmin = getCurrentAdmin();
        EmployeeUtils.checkPasswords(oldPassword, newPassword, currentAdmin.getPassword(), passwordEncoder);
        currentAdmin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(currentAdmin);
    }

    public AdminDTO updateCurrentUser(ChangeEmployeeDataRequestDTO changeUserDataRequestDTO) {
        Admin currentAdmin = getCurrentAdmin();
        EmployeeUtils.updateEmployeeFields(currentAdmin, changeUserDataRequestDTO, waiterRepository, adminRepository);
        Admin updatedAdmin = adminRepository.save(currentAdmin);
        return EmployeeUtils.convertAdminToAdminDTO(updatedAdmin);
    }

    private Waiter getWaiterWithIdAndIsDeletedCheck(Long id) {
        Waiter waiter = waiterRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Waiter with id: " + id + " is not found"));
        if (waiter.isDeleted()) {
            throw new EmployeeIsDeletedException("Waiter with id: " + id + " is deleted");
        }
        return waiter;
    }
}
