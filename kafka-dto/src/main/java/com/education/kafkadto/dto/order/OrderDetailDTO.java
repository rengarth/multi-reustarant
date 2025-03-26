package com.education.kafkadto.dto.order;

import com.education.kafkadto.dto.menu.DishDTO;
import lombok.Data;

@Data
public class OrderDetailDTO {
    private DishDTO dish;
    private Integer quantity;
    private Integer totalAmount;
}
