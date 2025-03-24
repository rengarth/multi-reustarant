package com.education.employee.util;

import com.education.employee.dto.table.RestTableDTO;
import com.education.employee.dto.table.RestTableItemDTO;
import com.education.employee.entity.table.RestTable;
import com.education.employee.entity.table.RestTableItem;

import java.util.stream.Collectors;

public class RestTableUtils {

    public static RestTableDTO convertRestTableToRestTableDTO(RestTable table) {
        RestTableDTO restTableDTO = new RestTableDTO();
        restTableDTO.setTableItems(
                table.getTableItems()
                        .stream()
                        .map(RestTableUtils::convertCartItemToCartItemDTO)
                        .collect(Collectors.toList()));
        restTableDTO.setTotalAmount(table.calculateTotalAmount());
        return restTableDTO;
    }


    private static RestTableItemDTO convertCartItemToCartItemDTO(RestTableItem restTableItem) {
        RestTableItemDTO restTableItemDTO = new RestTableItemDTO();
        restTableItemDTO.setDish(MenuUtils.convertDishToDishDTO(restTableItem.getDish()));
        restTableItemDTO.setQuantity(restTableItem.getQuantity());
        return restTableItemDTO;
    }

}
