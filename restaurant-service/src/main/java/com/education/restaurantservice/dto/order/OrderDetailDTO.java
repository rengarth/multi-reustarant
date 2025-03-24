package com.education.restaurantservice.dto.order;

import com.education.restaurantservice.dto.menu.DishDTO;
import lombok.Data;

@Data
public class OrderDetailDTO {
    private DishDTO dish;
    private Integer quantity;
    private Integer totalAmount;
}
