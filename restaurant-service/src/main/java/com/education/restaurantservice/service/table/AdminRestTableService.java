package com.education.restaurantservice.service.table;

import com.education.restaurantservice.dto.table.RestTableDTO;
import com.education.restaurantservice.entity.table.RestTable;
import com.education.restaurantservice.exception.table.RestTableNotFoundException;
import com.education.restaurantservice.repository.table.RestTableRepository;
import com.education.restaurantservice.util.RestTableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRestTableService {

    private final RestTableRepository restTableRepository;


    public List<RestTableDTO> getWaiterTables(Long id) {
        List<RestTable> waiterTables = restTableRepository.findRestTablesByWaiterId(id).orElse(new ArrayList<>());
        return waiterTables.stream().map(RestTableUtils::convertRestTableToRestTableDTO).toList();
    }

    public List<RestTableDTO> getAllTables() {
        List<RestTable> tables = restTableRepository.findAll();
        return tables.stream().map(RestTableUtils::convertRestTableToRestTableDTO).toList();
    }

    public void removeWaiterFromTable(Integer number) {
        RestTable table = restTableRepository.findByNumber(number)
                .orElseThrow(() -> new RestTableNotFoundException("Rest table with number: " + number + " not found"));
        table.setWaiter(null);
        table.getTableItems().clear();
        table.setTotalAmount(0);
        restTableRepository.save(table);
    }
}
