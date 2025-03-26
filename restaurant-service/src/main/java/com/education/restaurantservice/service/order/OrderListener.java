package com.education.restaurantservice.service.order;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.restaurantservice.entity.order.Order;
import com.education.restaurantservice.exception.order.OrderNotFoundException;
import com.education.restaurantservice.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderListener {

    private final OrderRepository orderRepository;


    @KafkaListener(topics = "order-updates", groupId = "order-service-group")
    public void handleOrderStatusUpdate(OrderDTO orderDTO) {
        Order order =
                orderRepository.findById(orderDTO
                        .getId())
                        .orElseThrow(() -> new OrderNotFoundException("Order not found"));
            order.setPaymentStatus(orderDTO.getPaymentStatus());
            order.setStatus(orderDTO.getStatus());
            order.setLeadTime(orderDTO.getLeadTime());
            orderRepository.save(order);
    }
}
