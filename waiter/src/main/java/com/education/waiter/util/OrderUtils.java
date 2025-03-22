package com.education.waiter.util;

import com.education.waiter.dto.order.OrderDTO;
import com.education.waiter.entity.order.Order;

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
