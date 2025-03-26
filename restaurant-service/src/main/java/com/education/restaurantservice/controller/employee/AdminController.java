package com.education.restaurantservice.controller.employee;

import com.education.kafkadto.dto.employee.WaiterDTO;
import com.education.restaurantservice.dto.employee.AdminDTO;
import com.education.restaurantservice.dto.employee.ChangeEmployeeDataRequestDTO;
import com.education.restaurantservice.dto.employee.ChangePasswordRequestDTO;
import com.education.restaurantservice.dto.employee.WaiterRegistrationDTO;
import com.education.restaurantservice.service.employee.AdminService;
import com.education.restaurantservice.util.EmployeeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all waiters", description = "Retrieve a list of all waiters.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of waiters",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = WaiterDTO.class))))
    @GetMapping("/waiters")
    public ResponseEntity<List<WaiterDTO>> getAllWaiters() {
        return ResponseEntity.ok(adminService.getAllWaiters());
    }

    @Operation(summary = "Get waiter by ID",
            description = "Retrieve waiter details by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Waiter successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WaiterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Waiter not found")
    })
    @GetMapping("/waiters/{id}")
    public ResponseEntity<WaiterDTO> getWaiterById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getWaiterById(id));
    }

    @Operation(summary = "Create a new waiter",
            description = "Register a new waiter.")
    @ApiResponse(responseCode = "201", description = "Waiter successfully created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = WaiterDTO.class)))
    @PostMapping("/waiters/create")
    public ResponseEntity<WaiterDTO> createWaiter(@RequestBody WaiterRegistrationDTO registrationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createWaiter(registrationDTO));
    }

    @Operation(summary = "Change waiter's password",
            description = "Change the password of a specific waiter.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password successfully changed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404",
                    description = "Waiter not found"),
            @ApiResponse(responseCode = "409",
                    description = "New password cannot be the same as the old password")
    })
    @PutMapping("/waiters/{id}/password")
    public ResponseEntity<Map<String, String>> changeWaiterPassword(
            @PathVariable Long id, @RequestBody ChangePasswordRequestDTO request) {
        adminService.changeWaiterPassword(id, request);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Password successfully changed");
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Update waiter data",
            description = "Update the details of an existing waiter by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Waiter successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WaiterDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Waiter not found")
    })
    @PutMapping("/waiters/{id}")
    public ResponseEntity<WaiterDTO> updateWaiter(
            @PathVariable Long id, @RequestBody ChangeEmployeeDataRequestDTO request) {
        return ResponseEntity.ok(adminService.updateWaiter(id, request));
    }

    @Operation(summary = "Delete waiter by ID",
            description = "Mark a waiter as deleted.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Waiter successfully marked as deleted"),
            @ApiResponse(responseCode = "404", description = "Waiter not found")
    })
    @DeleteMapping("/waiters/{id}")
    public ResponseEntity<Map<String, String>> deleteWaiter(@PathVariable Long id) {
        adminService.deleteWaiter(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Waiter successfully deleted");
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Restore deleted waiter",
            description = "Restore a deleted waiter by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Waiter successfully restored",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WaiterDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Waiter not found")
    })
    @PutMapping("/waiters/{id}/restore")
    public ResponseEntity<WaiterDTO> restoreWaiter(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.restoreWaiter(id));
    }

    @Operation(summary = "Get current admin",
            description = "Retrieve the details of the currently authenticated admin.")
    @ApiResponse(responseCode = "200",
            description = "Current admin successfully retrieved",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminDTO.class)))
    @GetMapping("/current")
    public ResponseEntity<AdminDTO> getCurrentAdmin() {
        return ResponseEntity.ok(EmployeeUtils.convertAdminToAdminDTO(adminService.getCurrentAdmin()));
    }

    @Operation(summary = "Change current admin password",
            description = "Change the password of the currently authenticated admin.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Password successfully changed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "409",
                    description = "New password cannot be the same as the old password")
    })
    @PutMapping("/current/change-password")
    public ResponseEntity<Map<String, String>> changeAdminPassword(
            @RequestBody ChangePasswordRequestDTO request) {
        adminService.changePassword(request);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Password successfully changed");
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Update current admin",
            description = "Update the details of the currently authenticated admin.")
    @ApiResponse(responseCode = "200", description = "Admin successfully updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminDTO.class)))
    @PutMapping("/current")
    public ResponseEntity<AdminDTO> updateCurrentAdmin(@RequestBody ChangeEmployeeDataRequestDTO request) {
        return ResponseEntity.ok(adminService.updateCurrentUser(request));
    }
}


