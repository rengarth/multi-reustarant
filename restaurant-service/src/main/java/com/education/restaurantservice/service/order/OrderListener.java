package com.education.restaurantservice.service.order;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.restaurantservice.entity.order.Order;
import com.education.restaurantservice.exception.order.OrderNotFoundException;
import com.education.restaurantservice.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderListener {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "order-updates", groupId = "order-service-group")
    public void handleOrderStatusUpdate(OrderDTO orderDTO) {
        log.info("Received order status update for order ID: {}...", orderDTO.getId());

        Order order = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> {
                    log.error("Order not found with id: {}", orderDTO.getId());
                    return new OrderNotFoundException("Order not found");
                });

        log.info("Updating order status, payment status, and lead time for order ID: {}...", orderDTO.getId());
        order.setPaymentStatus(orderDTO.getPaymentStatus());
        order.setStatus(orderDTO.getStatus());
        order.setLeadTime(orderDTO.getLeadTime());

        log.info("Saving updated order status for order ID: {}...", orderDTO.getId());
        orderRepository.save(order);
    }
}

