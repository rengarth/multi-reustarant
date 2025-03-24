package com.education.employee.service.table;

import com.education.employee.entity.employee.Waiter;
import com.education.employee.entity.table.RestTable;
import com.education.employee.exception.table.RestTableIsOccupiedException;
import com.education.employee.repository.table.RestTableRepository;
import com.education.employee.service.employee.WaiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestTableService {

    private final RestTableRepository restTableRepository;
    private final WaiterService waiterService;

    public void setTableToCurrentWaiter(Integer restTableNumber) {
        Waiter waiter = waiterService.getCurrentWaiter();
        RestTable table = restTableRepository.findByNumberAndWaiterIsNull(restTableNumber)
                .orElseThrow(() -> new RestTableIsOccupiedException(
                                "Table with number: "
                                + restTableNumber + " is occupied"));
        table.setWaiter(waiter);
        restTableRepository.save(table);
    }
}
