package com.education.waiter.util;

import com.education.waiter.dto.order.OrderDTO;
import com.education.waiter.dto.worker.AdminDTO;
import com.education.waiter.dto.worker.ChangeEmployeeDataRequestDTO;
import com.education.waiter.dto.worker.WaiterDTO;
import com.education.waiter.entity.employee.Admin;
import com.education.waiter.entity.employee.Employee;
import com.education.waiter.entity.employee.Waiter;
import com.education.waiter.exception.worker.IdenticalPasswordException;
import com.education.waiter.repository.employee.AdminRepository;
import com.education.waiter.repository.employee.WaiterRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeUtils {

    public static WaiterDTO convertWaiterToWaiterDTO(Waiter waiter) {
        WaiterDTO waiterDTO = new WaiterDTO();
        waiterDTO.setFirstName(waiter.getFirstName());
        waiterDTO.setLastName(waiter.getLastName());
        waiterDTO.setPhoneNumber(waiter.getPhoneNumber());
        waiterDTO.setIsDeleted(waiter.isDeleted());
        if (!waiter.getOrders().isEmpty()) {
            List<OrderDTO> orderDTOs = waiter.getOrders().stream()
                    .map(OrderUtils::convertOrderToOrderDTO)
                    .collect(Collectors.toList());
            waiterDTO.setOrders(orderDTOs);
        }
        else waiterDTO.setOrders(new ArrayList<>());
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
            throw new IllegalArgumentException("Неверный формат телефонного номера. Должен быть в формате +7**********.");
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
