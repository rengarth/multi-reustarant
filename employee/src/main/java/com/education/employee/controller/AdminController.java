package com.education.employee.controller;

import com.education.employee.dto.worker.*;
import com.education.employee.service.AdminService;
import com.education.employee.util.EmployeeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Получение всех официантов
    @GetMapping("/waiters")
    public ResponseEntity<List<WaiterDTO>> getAllWaiters() {
        List<WaiterDTO> waiters = adminService.getAllWaiters();
        return ResponseEntity.ok(waiters);
    }

    // Получение официанта по ID
    @GetMapping("/waiters/{id}")
    public ResponseEntity<WaiterDTO> getWaiterById(@PathVariable Long id) {
        WaiterDTO waiter = adminService.getWaiterById(id);
        return ResponseEntity.ok(waiter);
    }

    // Создание нового официанта
    @PostMapping("/waiters")
    public ResponseEntity<WaiterDTO> createWaiter(@RequestBody WaiterRegistrationDTO registrationDTO) {
        WaiterDTO createdWaiter = adminService.createWaiter(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWaiter);
    }

    // Изменение пароля официанта
    @PutMapping("/waiters/{id}/password")
    public ResponseEntity<Void> changeWaiterPassword(@PathVariable Long id,
                                                     @RequestBody ChangePasswordRequestDTO request) {
        adminService.changeWaiterPassword(id, request);
        return ResponseEntity.noContent().build();
    }

    // Обновление данных официанта
    @PutMapping("/waiters/{id}")
    public ResponseEntity<WaiterDTO> updateWaiter(@PathVariable Long id,
                                                  @RequestBody ChangeEmployeeDataRequestDTO changeEmployeeDataRequestDTO) {
        WaiterDTO updatedWaiter = adminService.updateWaiter(id, changeEmployeeDataRequestDTO);
        return ResponseEntity.ok(updatedWaiter);
    }

    // Удаление официанта
    @DeleteMapping("/waiters/{id}")
    public ResponseEntity<Void> deleteWaiter(@PathVariable Long id) {
        adminService.deleteWaiter(id);
        return ResponseEntity.noContent().build();
    }

    // Восстановление официанта
    @PutMapping("/waiters/{id}/restore")
    public ResponseEntity<WaiterDTO> restoreWaiter(@PathVariable Long id) {
        WaiterDTO restoredWaiter = adminService.restoreWaiter(id);
        return ResponseEntity.ok(restoredWaiter);
    }

    // Получение текущего администратора
    @GetMapping("/current")
    public ResponseEntity<AdminDTO> getCurrentAdmin() {
        AdminDTO currentAdmin = EmployeeUtils.convertAdminToAdminDTO(adminService.getCurrentAdmin());
        return ResponseEntity.ok(currentAdmin);
    }

    // Изменение пароля текущего администратора
    @PutMapping("/current/password")
    public ResponseEntity<Void> changeAdminPassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        adminService.changePassword(changePasswordRequestDTO);
        return ResponseEntity.noContent().build();
    }

    // Обновление данных текущего администратора
    @PutMapping("/current")
    public ResponseEntity<AdminDTO> updateCurrentAdmin(
            @RequestBody ChangeEmployeeDataRequestDTO changeUserDataRequestDTO) {
        AdminDTO updatedAdmin = adminService.updateCurrentUser(changeUserDataRequestDTO);
        return ResponseEntity.ok(updatedAdmin);
    }
}

