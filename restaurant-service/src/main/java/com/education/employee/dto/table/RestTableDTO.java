package com.education.employee.dto.table;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestTableDTO {
    private List<RestTableItemDTO> tableItems;
    private int totalAmount;
}
