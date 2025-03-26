package com.education.restaurantservice.controller.employee;

import com.education.restaurantservice.dto.employee.ChangeEmployeeDataRequestDTO;
import com.education.restaurantservice.dto.employee.ChangePasswordRequestDTO;
import com.education.kafkadto.dto.employee.WaiterDTO;
import com.education.kafkadto.dto.order.OrderDTO;
import com.education.restaurantservice.service.employee.WaiterService;
import com.education.restaurantservice.util.EmployeeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/waiters")
@RequiredArgsConstructor
public class WaiterController {

    private final WaiterService waiterService;

    @Operation(summary = "Get current waiter", description = "Retrieve the current authenticated waiter details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved current waiter",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WaiterDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Current waiter not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "410",
                    description = "Waiter has been deleted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/current")
    public ResponseEntity<WaiterDTO> getCurrentWaiter() {
        return ResponseEntity.ok(EmployeeUtils.convertWaiterToWaiterDTO(waiterService.getCurrentWaiter()));
    }

    @Operation(summary = "Get current waiter orders", description = "Retrieve a list of orders assigned to the current authenticated waiter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved orders for the current waiter",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class)))),
            @ApiResponse(responseCode = "404",
                    description = "Current waiter not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/current/orders")
    public ResponseEntity<List<OrderDTO>> getCurrentWaiterOrders() {
        return ResponseEntity.ok(waiterService.getCurrentWaiterOrders());
    }

    @Operation(summary = "Change password for current waiter", description = "Change the password of the current authenticated waiter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Password successfully changed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404",
                    description = "Current waiter not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "409",
                    description = "Conflict: Identical password or other conflict",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden: Insufficient privileges",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/current/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        waiterService.changePassword(request);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Password successfully changed");
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Update current waiter", description = "Update the profile details of the current authenticated waiter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Waiter successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WaiterDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Current waiter not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "409",
                    description = "Conflict: Data conflict or identical password",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden: Insufficient privileges",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/current")
    public ResponseEntity<WaiterDTO> updateCurrentWaiter(@RequestBody ChangeEmployeeDataRequestDTO request) {
        WaiterDTO updatedWaiter = waiterService.updateCurrentWaiter(request);
        return ResponseEntity.ok(updatedWaiter);
    }
}



