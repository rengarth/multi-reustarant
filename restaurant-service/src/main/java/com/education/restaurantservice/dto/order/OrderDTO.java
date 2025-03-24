package com.education.restaurantservice.dto.order;

import com.education.restaurantservice.entity.employee.Waiter;
import com.education.restaurantservice.entity.order.OrderStatus;
import com.education.restaurantservice.entity.order.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Waiter waiter;
    private List<OrderDetailDTO> orderDetails;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private int totalAmount;
    private LocalDateTime createTime;
    private LocalDateTime leadTime;
}
