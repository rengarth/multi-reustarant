package com.education.restaurantservice.dto.table;

import com.education.kafkadto.dto.menu.DishDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestTableItemDTO {
    private DishDTO dish;
    private Integer quantity;
}
