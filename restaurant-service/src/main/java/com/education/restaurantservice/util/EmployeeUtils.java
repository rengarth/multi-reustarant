package com.education.restaurantservice.util;

import com.education.restaurantservice.dto.employee.AdminDTO;
import com.education.restaurantservice.dto.employee.ChangeEmployeeDataRequestDTO;
import com.education.kafkadto.dto.employee.WaiterDTO;
import com.education.restaurantservice.entity.employee.Admin;
import com.education.restaurantservice.entity.employee.Employee;
import com.education.restaurantservice.entity.employee.Waiter;
import com.education.restaurantservice.exception.employee.IdenticalPasswordException;
import com.education.restaurantservice.repository.employee.AdminRepository;
import com.education.restaurantservice.repository.employee.WaiterRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EmployeeUtils {

    public static WaiterDTO convertWaiterToWaiterDTO(Waiter waiter) {
        WaiterDTO waiterDTO = new WaiterDTO();
        waiterDTO.setId(waiter.getId());
        waiterDTO.setFirstName(waiter.getFirstName());
        waiterDTO.setLastName(waiter.getLastName());
        waiterDTO.setPhoneNumber(waiter.getPhoneNumber());
        waiterDTO.setIsDeleted(waiter.isDeleted());
        return waiterDTO;
    }

    public static AdminDTO convertAdminToAdminDTO(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setFirstName(admin.getFirstName());
        adminDTO.setLastName(admin.getLastName());
        adminDTO.setPhoneNumber(admin.getPhoneNumber());
        adminDTO.setIsDeleted(admin.isDeleted());
        return adminDTO;
    }

    public static void checkPasswords(
            String oldPassword,
            String newPassword,
            String storedPassword,
            BCryptPasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(oldPassword, storedPassword)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        if (passwordEncoder.matches(newPassword, storedPassword)) {
            throw new IdenticalPasswordException("New password cannot be the same as old password");
        }
    }

    public static void updateEmployeeFields(
            Employee employee,
            ChangeEmployeeDataRequestDTO changeEmployeeDataRequestDTO,
            WaiterRepository waiterRepository,
            AdminRepository adminRepository) {

        if (changeEmployeeDataRequestDTO.getPhoneNumber() != null
                && !changeEmployeeDataRequestDTO.getPhoneNumber().equals(employee.getPhoneNumber())) {
            validatePhoneNumber(changeEmployeeDataRequestDTO.getPhoneNumber());
            checkPhoneNumberExists(changeEmployeeDataRequestDTO.getPhoneNumber(), waiterRepository, adminRepository);
            employee.setPhoneNumber(changeEmployeeDataRequestDTO.getPhoneNumber());
        }

        if (changeEmployeeDataRequestDTO.getFirstName() != null
                && !changeEmployeeDataRequestDTO.getFirstName().equals(employee.getFirstName())) {
            employee.setFirstName(changeEmployeeDataRequestDTO.getFirstName());
        }

        if (changeEmployeeDataRequestDTO.getLastName() != null
                && !changeEmployeeDataRequestDTO.getLastName().equals(employee.getLastName())) {
            employee.setLastName(changeEmployeeDataRequestDTO.getLastName());
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("^\\+7\\d{10}$")) {
            throw new IllegalArgumentException("Wrong phone number format. It must be +7**********");
        }
    }

    public static void checkPhoneNumberExists(
            String phoneNumber,
            WaiterRepository waiterRepository,
            AdminRepository adminRepository) {
        if (waiterRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("This phone number is already in use by another waiter");
        }
        if (adminRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("This phone number is already in use by another admin");
        }
    }
}
