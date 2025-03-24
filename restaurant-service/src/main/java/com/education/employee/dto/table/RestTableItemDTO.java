package com.education.employee.dto.table;

import com.education.employee.dto.menu.DishDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestTableItemDTO {
    private DishDTO dish;
    private Integer quantity;
}
