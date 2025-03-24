package com.education.employee.service.table;

import com.education.employee.entity.table.RestTableItem;
import com.education.employee.repository.menu.DishRepository;
import com.education.employee.repository.table.RestTableItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestTableItemService {

    private final RestTableItemRepository restTableItemRepository;
    private final DishRepository dishRepository;

}
