package com.education.kafkadto.dto.order;

import com.education.kafkadto.dto.employee.WaiterDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private WaiterDTO waiter;
    private Integer tableNumber;
    private List<OrderDetailDTO> orderDetails;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private int totalAmount;
    private LocalDateTime createTime;
    private LocalDateTime leadTime;
}
