package com.education.employee.controller.employee;

import com.education.employee.dto.employee.*;
import com.education.employee.service.employee.AdminService;
import com.education.employee.util.EmployeeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/waiters")
    public ResponseEntity<List<WaiterDTO>> getAllWaiters() {
        List<WaiterDTO> waiters = adminService.getAllWaiters();
        return ResponseEntity.ok(waiters);
    }

    @GetMapping("/waiters/{id}")
    public ResponseEntity<WaiterDTO> getWaiterById(@PathVariable Long id) {
        WaiterDTO waiter = adminService.getWaiterById(id);
        return ResponseEntity.ok(waiter);
    }

    @PostMapping("/waiters")
    public ResponseEntity<WaiterDTO> createWaiter(@RequestBody WaiterRegistrationDTO registrationDTO) {
        WaiterDTO createdWaiter = adminService.createWaiter(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWaiter);
    }

    @PutMapping("/waiters/{id}/password")
    public ResponseEntity<Map<String, String>> changeWaiterPassword(@PathVariable Long id,
                                                     @RequestBody ChangePasswordRequestDTO request) {
        adminService.changeWaiterPassword(id, request);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Password successfully changed");
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/waiters/{id}")
    public ResponseEntity<WaiterDTO> updateWaiter(@PathVariable Long id,
                                                  @RequestBody ChangeEmployeeDataRequestDTO changeEmployeeDataRequestDTO) {
        WaiterDTO updatedWaiter = adminService.updateWaiter(id, changeEmployeeDataRequestDTO);
        return ResponseEntity.ok(updatedWaiter);
    }

    @DeleteMapping("/waiters/{id}")
    public ResponseEntity<Map<String, String>> deleteWaiter(@PathVariable Long id) {
        adminService.deleteWaiter(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Waiter successfully deleted");
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/waiters/{id}/restore")
    public ResponseEntity<WaiterDTO> restoreWaiter(@PathVariable Long id) {
        WaiterDTO restoredWaiter = adminService.restoreWaiter(id);
        return ResponseEntity.ok(restoredWaiter);
    }

    @GetMapping("/current")
    public ResponseEntity<AdminDTO> getCurrentAdmin() {
        AdminDTO currentAdmin = EmployeeUtils.convertAdminToAdminDTO(adminService.getCurrentAdmin());
        return ResponseEntity.ok(currentAdmin);
    }

    @PutMapping("/current/change-password")
    public ResponseEntity<Map<String, String>> changeAdminPassword(
            @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        adminService.changePassword(changePasswordRequestDTO);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Password successfully changed");
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/current")
    public ResponseEntity<AdminDTO> updateCurrentAdmin(
            @RequestBody ChangeEmployeeDataRequestDTO changeUserDataRequestDTO) {
        AdminDTO updatedAdmin = adminService.updateCurrentUser(changeUserDataRequestDTO);
        return ResponseEntity.ok(updatedAdmin);
    }
}

