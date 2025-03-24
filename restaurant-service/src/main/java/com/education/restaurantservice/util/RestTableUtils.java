package com.education.restaurantservice.util;

import com.education.restaurantservice.dto.table.RestTableDTO;
import com.education.restaurantservice.dto.table.RestTableItemDTO;
import com.education.restaurantservice.entity.table.RestTable;
import com.education.restaurantservice.entity.table.RestTableItem;

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
