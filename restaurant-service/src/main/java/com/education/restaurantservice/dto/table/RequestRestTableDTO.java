package com.education.restaurantservice.dto.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRestTableDTO {
    private Integer tableNumber;
    private Long dishId;
    private Integer quantity;
}
