package com.education.restaurantservice.dto.table;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestTableDTO {
    private Integer number;
    private List<RestTableItemDTO> tableItems;
    private int totalAmount;
}
