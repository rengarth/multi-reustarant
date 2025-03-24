package com.education.employee.controller.table;

import com.education.employee.dto.table.RequestRestTableDTO;
import com.education.employee.dto.table.RestTableDTO;
import com.education.employee.service.table.RestTableService;
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

    @PostMapping("/{number}/assign")
    public ResponseEntity<Map<String, String>> assignTableToCurrentWaiter(@PathVariable Integer number) {
        restTableService.setTableToCurrentWaiter(number);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Table with number: " + number + " is assigned to current waiter");
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{number}")
    public ResponseEntity<RestTableDTO> getTable(@PathVariable Integer number) {
        RestTableDTO table = restTableService.getTableDTO(number);
        return ResponseEntity.ok(table);
    }

    @PostMapping("/{number}/calculate")
    public ResponseEntity<RestTableDTO> calculateTable(
            @PathVariable Integer number,
            @RequestBody RequestRestTableDTO tableDTO) {
        RestTableDTO updatedTable = restTableService.calculateTable(number, tableDTO);
        return ResponseEntity.ok(updatedTable);
    }

    @DeleteMapping("/{number}/clear")
    public ResponseEntity<Void> clearTable(@PathVariable Integer number) {
        restTableService.clearTable(number);
        return ResponseEntity.noContent().build();
    }
}