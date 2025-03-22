package com.education.employee.util;

import com.education.employee.dto.order.OrderDTO;
import com.education.employee.entity.order.Order;

public class OrderUtils {

    public static OrderDTO convertOrderToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setWaiter(order.getWaiter());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalQuantity(order.getTotalQuantity());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setCreateTime(order.getCreateTime());
        orderDTO.setLeadTime(order.getLeadTime());
        return orderDTO;
    }
}
