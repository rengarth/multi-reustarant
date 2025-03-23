package com.education.employee.service.employee;

import com.education.employee.dto.employee.ChangeEmployeeDataRequestDTO;
import com.education.employee.dto.employee.ChangePasswordRequestDTO;
import com.education.employee.dto.employee.WaiterDTO;
import com.education.employee.entity.employee.Waiter;
import com.education.employee.exception.employee.EmployeeNotFoundException;
import com.education.employee.repository.employee.AdminRepository;
import com.education.employee.repository.employee.WaiterRepository;
import com.education.employee.util.EmployeeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaiterService {

    private final WaiterRepository waiterRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Waiter getCurrentWaiter() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("Current waiter not found in security context");
        }
        String phoneNumber = ((UserDetails) authentication.getPrincipal()).getUsername();
        return waiterRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Waiter with phone number: " + phoneNumber + " is not found"));
    }

    public void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) {
        String oldPassword = changePasswordRequestDTO.getOldPassword();
        String newPassword = changePasswordRequestDTO.getNewPassword();
        Waiter currentUser = getCurrentWaiter();
        EmployeeUtils.checkPasswords(oldPassword, newPassword, currentUser.getPassword(), passwordEncoder);
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        waiterRepository.save(currentUser);
    }

    public WaiterDTO updateCurrentWaiter(ChangeEmployeeDataRequestDTO changeEmployeeDataRequestDTO) {
        Waiter currentWaiter = getCurrentWaiter();
        EmployeeUtils.updateEmployeeFields(currentWaiter, changeEmployeeDataRequestDTO, waiterRepository, adminRepository);
        Waiter updatedWaiter = waiterRepository.save(currentWaiter);
        return EmployeeUtils.convertWaiterToWaiterDTO(updatedWaiter);
    }
}
