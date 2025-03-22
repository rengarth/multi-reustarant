package com.education.employee.dto.order;

import com.education.employee.entity.employee.Waiter;
import com.education.employee.entity.order.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private Waiter waiter;
    private OrderStatus status = OrderStatus.WAITING_FOR_PAYMENT;
    private int totalQuantity;
    private int totalPrice;
    private LocalDateTime createTime;
    private LocalDateTime leadTime;
}
