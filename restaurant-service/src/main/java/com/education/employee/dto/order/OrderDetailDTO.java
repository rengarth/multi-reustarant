package com.education.employee.dto.order;

import com.education.employee.dto.menu.DishDTO;
import lombok.Data;

@Data
public class OrderDetailDTO {
    private DishDTO dish;
    private Integer quantity;
    private Integer totalAmount;
}
