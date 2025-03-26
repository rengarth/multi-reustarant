package com.education.restaurantservice.service.employee;

import com.education.kafkadto.dto.employee.WaiterDTO;
import com.education.kafkadto.dto.order.OrderDTO;
import com.education.restaurantservice.dto.employee.ChangeEmployeeDataRequestDTO;
import com.education.restaurantservice.dto.employee.ChangePasswordRequestDTO;
import com.education.restaurantservice.entity.employee.Waiter;
import com.education.restaurantservice.exception.employee.EmployeeNotFoundException;
import com.education.restaurantservice.repository.employee.AdminRepository;
import com.education.restaurantservice.repository.employee.WaiterRepository;
import com.education.restaurantservice.repository.order.OrderRepository;
import com.education.restaurantservice.util.EmployeeUtils;
import com.education.restaurantservice.util.OrderUtils;
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
public class WaiterService {

    private final WaiterRepository waiterRepository;
    private final AdminRepository adminRepository;
    private final OrderRepository orderRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Waiter getCurrentWaiter() {
        log.info("Fetching current waiter from security context...");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            log.error("Current waiter not found in security context");
            throw new EmployeeNotFoundException("Current waiter not found in security context");
        }

        String phoneNumber = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.info("Extract phone number from authentication: {}", phoneNumber);

        return waiterRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> {
                    log.error("Waiter with phone number {} is not found", phoneNumber);
                    return new EmployeeNotFoundException("Waiter with phone number: " + phoneNumber + " is not found");
                });
    }

    public List<OrderDTO> getCurrentWaiterOrders() {
        Long currentWaiterId = getCurrentWaiter().getId();
        log.info("Fetching orders for waiter with ID: {}...", currentWaiterId);

        List<OrderDTO> orders = orderRepository.findOrdersByWaiterId(currentWaiterId)
                .stream()
                .map(OrderUtils::convertOrderToOrderDTO)
                .toList();

        log.info("Found {} orders for waiter with ID: {}", orders.size(), currentWaiterId);
        return orders;
    }

    public void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) {
        log.info("Changing password for current waiter...");

        String oldPassword = changePasswordRequestDTO.getOldPassword();
        String newPassword = changePasswordRequestDTO.getNewPassword();

        Waiter currentWaiter = getCurrentWaiter();
        log.info("Validating old password for waiter with ID: {}...", currentWaiter.getId());

        EmployeeUtils.checkPasswords(oldPassword, newPassword, currentWaiter.getPassword(), passwordEncoder);

        log.info("Password validation successful, updating password for waiter with ID: {}", currentWaiter.getId());
        currentWaiter.setPassword(passwordEncoder.encode(newPassword));

        log.info("Saving updated password for waiter with ID: {}", currentWaiter.getId());
        waiterRepository.save(currentWaiter);

        log.info("Password updated successfully for waiter with ID: {}", currentWaiter.getId());
    }

    public WaiterDTO updateCurrentWaiter(ChangeEmployeeDataRequestDTO changeEmployeeDataRequestDTO) {
        log.info("Updating current waiter data...");

        Waiter currentWaiter = getCurrentWaiter();
        log.info("Found current waiter with ID: {}", currentWaiter.getId());

        EmployeeUtils.updateEmployeeFields(currentWaiter, changeEmployeeDataRequestDTO, waiterRepository, adminRepository);

        log.info("Saving updated waiter data for ID: {}", currentWaiter.getId());
        Waiter updatedWaiter = waiterRepository.save(currentWaiter);

        log.info("Waiter data updated successfully for ID: {}", updatedWaiter.getId());
        return EmployeeUtils.convertWaiterToWaiterDTO(updatedWaiter);
    }
}

