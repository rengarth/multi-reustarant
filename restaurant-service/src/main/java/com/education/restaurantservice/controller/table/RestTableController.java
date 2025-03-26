package com.education.restaurantservice.controller.table;

import com.education.restaurantservice.dto.table.RequestRestTableDTO;
import com.education.restaurantservice.dto.table.RestTableDTO;
import com.education.restaurantservice.service.table.RestTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class RestTableController {

    private final RestTableService restTableService;

    @Operation(summary = "Assign table to the current waiter",
            description = "Assign a table to the currently authenticated waiter by table number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully assigned the table to the current waiter",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Table not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{number}/assign")
    public ResponseEntity<Map<String, String>> assignTableToCurrentWaiter(@PathVariable Integer number) {
        restTableService.setTableToCurrentWaiter(number);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Table with number: " + number + " is assigned to current waiter");
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Get table details",
            description = "Retrieve details of a specific table by its number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved table details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestTableDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Table not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping("/{number}")
    public ResponseEntity<RestTableDTO> getTable(@PathVariable Integer number) {
        RestTableDTO table = restTableService.getTable(number);
        return ResponseEntity.ok(table);
    }

    @Operation(summary = "Calculate table total",
            description = "Calculate and update the total for a specific table by its number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully calculated and updated table total",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestTableDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid data in the request"),
            @ApiResponse(responseCode = "404",
                    description = "Table not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PostMapping("/{number}/calculate")
    public ResponseEntity<RestTableDTO> calculateTable(
            @PathVariable Integer number,
            @RequestBody RequestRestTableDTO tableDTO) {
        RestTableDTO updatedTable = restTableService.calculateTable(number, tableDTO);
        return ResponseEntity.ok(updatedTable);
    }

    @Operation(summary = "Clear table", description = "Clear the contents of a specific table by its number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully cleared the table"),
            @ApiResponse(responseCode = "404", description = "Table not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{number}/clear")
    public ResponseEntity<Void> clearTable(@PathVariable Integer number) {
        restTableService.clearTable(number);
        return ResponseEntity.noContent().build();
    }
}
