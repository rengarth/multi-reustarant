package com.education.employee.controller.employee;

import com.education.employee.dto.employee.ChangeEmployeeDataRequestDTO;
import com.education.employee.dto.employee.ChangePasswordRequestDTO;
import com.education.employee.dto.employee.WaiterDTO;
import com.education.employee.dto.order.OrderDTO;
import com.education.employee.service.employee.WaiterService;
import com.education.employee.util.EmployeeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/waiter")
@RequiredArgsConstructor
public class WaiterController {

    private final WaiterService waiterService;

    @GetMapping("/current")
    public ResponseEntity<WaiterDTO> getCurrentWaiter() {
        return ResponseEntity.ok(EmployeeUtils.convertWaiterToWaiterDTO(waiterService.getCurrentWaiter()));
    }

    @GetMapping("/current/orders")
    public ResponseEntity<List<OrderDTO>> getCurrentWaiterOrders() {
        return ResponseEntity.ok(waiterService.getCurrentWaiterOrders());
    }

    @PutMapping("/current/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        waiterService.changePassword(request);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Password successfully changed");
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/current")
    public ResponseEntity<WaiterDTO> updateCurrentWaiter(@RequestBody ChangeEmployeeDataRequestDTO request) {
        WaiterDTO updatedWaiter = waiterService.updateCurrentWaiter(request);
        return ResponseEntity.ok(updatedWaiter);
    }
}

