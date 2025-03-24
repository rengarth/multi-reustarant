package com.education.restaurantservice.util;

import com.education.restaurantservice.dto.order.OrderDTO;
import com.education.restaurantservice.dto.order.OrderDetailDTO;
import com.education.restaurantservice.entity.order.Order;
import com.education.restaurantservice.entity.order.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class OrderUtils {

    public static OrderDTO convertOrderToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setWaiter(order.getWaiter());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPaymentStatus(order.getPaymentStatus());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setCreateTime(order.getCreateTime());
        orderDTO.setLeadTime(order.getLeadTime());
        List<OrderDetail> orderDetails = order.getOrderDetails();
        List<OrderDetailDTO> orderDetailsDTO = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailsDTO.add(convertOrderDetailToOrderDetailDTO(orderDetail));
        }
        orderDTO.setOrderDetails(orderDetailsDTO);
        return orderDTO;
    }

    public static OrderDetailDTO convertOrderDetailToOrderDetailDTO(OrderDetail orderDetail) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setDish(MenuUtils.convertDishToDishDTO(orderDetail.getDish()));
        orderDetailDTO.setQuantity(orderDetail.getQuantity());
        orderDetailDTO.setTotalAmount(orderDetail.getTotalAmount());
        return orderDetailDTO;
    }
}
