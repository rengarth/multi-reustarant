package com.education.restaurantservice.service.employee;

import com.education.kafkadto.dto.employee.WaiterDTO;
import com.education.restaurantservice.dto.employee.*;
import com.education.restaurantservice.entity.employee.Admin;
import com.education.restaurantservice.entity.employee.Waiter;
import com.education.restaurantservice.exception.employee.EmployeeIsDeletedException;
import com.education.restaurantservice.exception.employee.EmployeeNotFoundException;
import com.education.restaurantservice.repository.employee.AdminRepository;
import com.education.restaurantservice.repository.employee.WaiterRepository;
import com.education.restaurantservice.util.EmployeeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final WaiterRepository waiterRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<WaiterDTO> getAllWaiters() {
        log.info("Fetching all waiters...");
        return waiterRepository.findAll().stream()
                .map(EmployeeUtils::convertWaiterToWaiterDTO)
                .toList();
    }

    public WaiterDTO getWaiterById(Long id) {
        log.info("Fetching waiter with ID: {}...", id);
        Waiter waiter = waiterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Waiter with ID: {} not found", id);
                    return new EmployeeNotFoundException("Waiter with id: " + id + " not found");
                });
        return EmployeeUtils.convertWaiterToWaiterDTO(waiter);
    }

    public WaiterDTO createWaiter(WaiterRegistrationDTO registrationDTO) {
        log.info("Creating new waiter with phone number: {}...", registrationDTO.getPhoneNumber());

        EmployeeUtils.validatePhoneNumber(registrationDTO.getPhoneNumber());
        EmployeeUtils.checkPhoneNumberExists(registrationDTO.getPhoneNumber(), waiterRepository, adminRepository);

        Waiter waiter = new Waiter();
        waiter.setPhoneNumber(registrationDTO.getPhoneNumber());
        waiter.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        waiter.setFirstName(registrationDTO.getFirstName());
        waiter.setLastName(registrationDTO.getLastName());

        log.info("Saving new waiter with phone number: {}...", registrationDTO.getPhoneNumber());
        Waiter savedWaiter = waiterRepository.save(waiter);

        log.info("Waiter created successfully with ID: {}", savedWaiter.getId());
        return EmployeeUtils.convertWaiterToWaiterDTO(savedWaiter);
    }

    public void changeWaiterPassword(Long id, ChangePasswordRequestDTO request) {
        log.info("Changing password for waiter with ID: {}...", id);
        Waiter waiter = waiterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Waiter with ID: {} not found", id);
                    return new EmployeeNotFoundException("User with id: " + id + " not found");
                });

        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        EmployeeUtils.checkPasswords(oldPassword, newPassword, waiter.getPassword(), passwordEncoder);

        log.info("Updating password for waiter with ID: {}...", id);
        waiter.setPassword(passwordEncoder.encode(newPassword));

        log.info("Saving updated password for waiter with ID: {}...", id);
        waiterRepository.save(waiter);

        log.info("Password updated successfully for waiter with ID: {}", id);
    }

    public WaiterDTO updateWaiter(Long id, ChangeEmployeeDataRequestDTO changeEmployeeDataRequestDTO) {
        log.info("Updating waiter with ID: {}...", id);
        Waiter waiter = waiterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Waiter with ID: {} not found", id);
                    return new EmployeeNotFoundException("Waiter with id: " + id + " not found");
                });

        EmployeeUtils.updateEmployeeFields(waiter, changeEmployeeDataRequestDTO, waiterRepository, adminRepository);

        log.info("Saving updated waiter data for ID: {}...", id);
        Waiter updatedWaiter = waiterRepository.save(waiter);

        log.info("Waiter data updated successfully for ID: {}", id);
        return EmployeeUtils.convertWaiterToWaiterDTO(updatedWaiter);
    }

    public void deleteWaiter(Long id) {
        log.info("Marking waiter with ID: {} as deleted...", id);
        Waiter waiter = getWaiterWithIdAndIsDeletedCheck(id);
        waiter.setDeleted(true);

        log.info("Saving deleted status for waiter with ID: {}...", id);
        waiterRepository.save(waiter);

        log.info("Waiter with ID: {} marked as deleted", id);
    }

    public WaiterDTO restoreWaiter(Long id) {
        log.info("Restoring waiter with ID: {}...", id);
        Waiter waiter = waiterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Waiter with ID: {} not found", id);
                    return new EmployeeNotFoundException("Waiter with id: " + id + " not found");
                });

        if (!waiter.isDeleted()) {
            log.warn("Waiter with ID: {} is not deleted, restoration not needed", id);
            throw new IllegalArgumentException("Waiter is not deleted");
        }

        waiter.setDeleted(false);

        log.info("Saving restored waiter with ID: {}...", id);
        waiterRepository.save(waiter);

        log.info("Waiter restored successfully with ID: {}", id);
        return EmployeeUtils.convertWaiterToWaiterDTO(waiter);
    }

    public Admin getCurrentAdmin() {
        log.info("Fetching current admin from security context...");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            log.error("Current admin not found in security context");
            throw new EmployeeNotFoundException("Current user not found in security context");
        }

        String phoneNumber = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.info("Extracted phone number for admin: {}", phoneNumber);

        return adminRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> {
                    log.error("Admin with phone number {} not found", phoneNumber);
                    return new EmployeeNotFoundException("User with phone number: " + phoneNumber + " is not found");
                });
    }

    public void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) {
        log.info("Changing password for current admin...");
        String oldPassword = changePasswordRequestDTO.getOldPassword();
        String newPassword = changePasswordRequestDTO.getNewPassword();
        Admin currentAdmin = getCurrentAdmin();

        EmployeeUtils.checkPasswords(oldPassword, newPassword, currentAdmin.getPassword(), passwordEncoder);

        log.info("Updating password for current admin...");
        currentAdmin.setPassword(passwordEncoder.encode(newPassword));

        log.info("Saving updated password for current admin...");
        adminRepository.save(currentAdmin);

        log.info("Password updated successfully for current admin");
    }

    public AdminDTO updateCurrentUser(ChangeEmployeeDataRequestDTO changeUserDataRequestDTO) {
        log.info("Updating current admin data...");
        Admin currentAdmin = getCurrentAdmin();
        EmployeeUtils.updateEmployeeFields(currentAdmin, changeUserDataRequestDTO, waiterRepository, adminRepository);

        log.info("Saving updated admin data...");
        Admin updatedAdmin = adminRepository.save(currentAdmin);

        log.info("Admin data updated successfully");
        return EmployeeUtils.convertAdminToAdminDTO(updatedAdmin);
    }

    private Waiter getWaiterWithIdAndIsDeletedCheck(Long id) {
        log.info("Fetching waiter with ID: {} for deletion check...", id);
        Waiter waiter = waiterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Waiter with ID: {} not found", id);
                    return new EmployeeNotFoundException("Waiter with id: " + id + " is not found");
                });

        if (waiter.isDeleted()) {
            log.warn("Waiter with ID: {} is already deleted", id);
            throw new EmployeeIsDeletedException("Waiter with id: " + id + " is deleted");
        }

        return waiter;
    }
}

