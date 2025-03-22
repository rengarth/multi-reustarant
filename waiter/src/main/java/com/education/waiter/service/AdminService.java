package com.education.waiter.service;

import com.education.waiter.dto.worker.*;
import com.education.waiter.entity.employee.Admin;
import com.education.waiter.entity.employee.Waiter;
import com.education.waiter.exception.worker.EmployeeIsDeletedException;
import com.education.waiter.exception.worker.EmployeeNotFoundException;
import com.education.waiter.repository.employee.AdminRepository;
import com.education.waiter.repository.employee.WaiterRepository;
import com.education.waiter.util.EmployeeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final WaiterRepository waiterRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Admin admin = getAdminWithPhoneNumberAndIsDeletedCheck(phoneNumber);
        return new org.springframework.security.core.userdetails.User(
                admin.getPhoneNumber(),
                admin.getPassword(),
                List.of(new SimpleGrantedAuthority("ADMIN")));
    }

    public List<WaiterDTO> getAllWaiters() {
        return waiterRepository.findAll().stream()
                .map(EmployeeUtils::convertWaiterToWaiterDTO)
                .toList();
    }

    public WaiterDTO getWaiterById(Long id) {
        Waiter user = waiterRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Waiter with id: " + id + " not found"));
        return EmployeeUtils.convertWaiterToWaiterDTO(user);
    }

    public WaiterDTO createWaiter(WaiterRegistrationDTO registrationDTO) {
        EmployeeUtils.validatePhoneNumber(registrationDTO.getPhoneNumber());
        EmployeeUtils.checkPhoneNumberExists(registrationDTO.getPhoneNumber(), waiterRepository, adminRepository);

        Waiter waiter = new Waiter();
        waiter.setPhoneNumber(registrationDTO.getPhoneNumber());
        waiter.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        waiter.setFirstName(registrationDTO.getFirstName());
        waiter.setLastName(registrationDTO.getLastName());
//        waiter.setDeleted(false);
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

    private Admin getAdminWithPhoneNumberAndIsDeletedCheck(String phoneNumber) {
        Admin admin = adminRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Admin with phone number: " + phoneNumber + " is not found"));
        if (admin.isDeleted()) {
            throw new EmployeeIsDeletedException("Admin with phone number: " + phoneNumber + " is deleted");
        }
        return admin;
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
