package com.education.restaurantservice.service.table;

import com.education.restaurantservice.dto.table.RestTableDTO;
import com.education.restaurantservice.entity.table.RestTable;
import com.education.restaurantservice.exception.table.RestTableNotFoundException;
import com.education.restaurantservice.repository.table.RestTableRepository;
import com.education.restaurantservice.util.RestTableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRestTableService {

    private final RestTableRepository restTableRepository;

    public List<RestTableDTO> getWaiterTables(Long id) {
        log.info("Fetching tables for waiter with id: {}...", id);
        List<RestTable> waiterTables = restTableRepository.findRestTablesByWaiterId(id).orElse(new ArrayList<>());
        log.info("Retrieved {} tables for waiter with id: {}", waiterTables.size(), id);
        return waiterTables.stream()
                .map(RestTableUtils::convertRestTableToRestTableDTO)
                .toList();
    }

    public List<RestTableDTO> getAllTables() {
        log.info("Fetching all tables...");
        List<RestTable> tables = restTableRepository.findAll();
        log.info("Retrieved {} tables.", tables.size());
        return tables.stream()
                .map(RestTableUtils::convertRestTableToRestTableDTO)
                .toList();
    }

    public void removeWaiterFromTable(Integer number) {
        log.info("Removing waiter from table with number: {}...", number);
        RestTable table = restTableRepository.findByNumber(number)
                .orElseThrow(() -> {
                    log.error("Rest table with number: {} not found", number);
                    return new RestTableNotFoundException("Rest table with number: " + number + " not found");
                });
        table.setWaiter(null);
        table.getTableItems().clear();
        table.setTotalAmount(0);
        restTableRepository.save(table);
        log.info("Waiter removed from table with number: {} and table cleared.", number);
    }
}
