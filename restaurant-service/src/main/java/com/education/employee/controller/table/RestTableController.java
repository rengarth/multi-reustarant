package com.education.employee.controller.table;

import com.education.employee.service.table.RestTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class RestTableController {

    private final RestTableService restTableService;

    @PostMapping("/{tableNumber}/assign")
    public ResponseEntity<Map<String, String>> assignTableToCurrentWaiter(@PathVariable Integer tableNumber) {
        restTableService.setTableToCurrentWaiter(tableNumber);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Table with number: " + tableNumber + " is assigned to current waiter");
        return ResponseEntity.ok(responseBody);
    }
}