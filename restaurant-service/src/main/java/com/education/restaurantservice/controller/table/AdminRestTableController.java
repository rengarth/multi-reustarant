package com.education.restaurantservice.controller.table;

import com.education.restaurantservice.dto.table.RestTableDTO;
import com.education.restaurantservice.service.table.AdminRestTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tables")
@RequiredArgsConstructor
public class AdminRestTableController {

    private final AdminRestTableService adminRestTableService;


    @GetMapping("/waiter/{id}")
    public ResponseEntity<List<RestTableDTO>> getWaiterTables(@PathVariable Long id) {
        List<RestTableDTO> waiterTables = adminRestTableService.getWaiterTables(id);
        return ResponseEntity.ok(waiterTables);
    }


    @GetMapping
    public ResponseEntity<List<RestTableDTO>> getAllTables() {
        List<RestTableDTO> tables = adminRestTableService.getAllTables();
        return ResponseEntity.ok(tables);
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Void> removeWaiterFromTable(@PathVariable Integer number) {
        adminRestTableService.removeWaiterFromTable(number);
        return ResponseEntity.noContent().build();
    }
}
