package com.education.restaurantservice.service.order;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.restaurantservice.entity.order.Order;
import com.education.restaurantservice.exception.order.OrderNotFoundException;
import com.education.restaurantservice.repository.order.OrderRepository;
import com.education.restaurantservice.util.OrderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepository;

    public List<OrderDTO> getAllOrders() {
        log.info("Fetching all orders...");
        List<OrderDTO> orders = orderRepository.findAll().stream()
                .map(OrderUtils::convertOrderToOrderDTO)
                .toList();
        log.info("Retrieved {} orders.", orders.size());
        return orders;
    }

    public OrderDTO getOrderById(Long id) {
        log.info("Fetching order by id: {}...", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order with id: {} not found", id);
                    return new OrderNotFoundException("Order with id: " + id + " not found");
                });
        log.info("Found order with id: {}. Converting to DTO...", id);
        return OrderUtils.convertOrderToOrderDTO(order);
    }

    public List<OrderDTO> getWaiterOrders(Long id) {
        log.info("Fetching orders for waiter with id: {}...", id);
        List<OrderDTO> orders = orderRepository
                .findOrdersByWaiterId(id)
                .stream()
                .map(OrderUtils::convertOrderToOrderDTO)
                .toList();
        log.info("Retrieved {} orders for waiter with id: {}", orders.size(), id);
        return orders;
    }
}

