package com.education.restaurantservice.controller.table;

import com.education.restaurantservice.dto.table.RestTableDTO;
import com.education.restaurantservice.service.table.AdminRestTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tables")
@RequiredArgsConstructor
public class AdminRestTableController {

    private final AdminRestTableService adminRestTableService;

    @Operation(summary = "Get all tables assigned to a specific waiter", description = "Retrieve a list of tables assigned to a waiter by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tables for the waiter",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestTableDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Waiter or tables not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/waiter/{id}")
    public ResponseEntity<List<RestTableDTO>> getWaiterTables(@PathVariable Long id) {
        List<RestTableDTO> waiterTables = adminRestTableService.getWaiterTables(id);
        return ResponseEntity.ok(waiterTables);
    }

    @Operation(summary = "Get all tables", description = "Retrieve a list of all tables.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all tables",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestTableDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<RestTableDTO>> getAllTables() {
        List<RestTableDTO> tables = adminRestTableService.getAllTables();
        return ResponseEntity.ok(tables);
    }

    @Operation(summary = "Remove waiter from table",
            description = "Remove a waiter from a table by its number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully removed waiter from table"),
            @ApiResponse(responseCode = "404", description = "Table not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{number}")
    public ResponseEntity<Void> removeWaiterFromTable(@PathVariable Integer number) {
        adminRestTableService.removeWaiterFromTable(number);
        return ResponseEntity.noContent().build();
    }
}

