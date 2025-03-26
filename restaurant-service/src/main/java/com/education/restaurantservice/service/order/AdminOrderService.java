package com.education.restaurantservice.service.order;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.restaurantservice.entity.order.Order;
import com.education.restaurantservice.exception.order.OrderNotFoundException;
import com.education.restaurantservice.repository.order.OrderRepository;
import com.education.restaurantservice.util.OrderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepository;


    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderUtils::convertOrderToOrderDTO).toList();
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Order with id: " + id + " not found"));
        return OrderUtils.convertOrderToOrderDTO(order);
    }

    public List<OrderDTO> getWaiterOrders(Long id) {
        return orderRepository
                .findOrdersByWaiterId(id)
                .stream()
                .map(OrderUtils::convertOrderToOrderDTO)
                .toList();
    }
}
