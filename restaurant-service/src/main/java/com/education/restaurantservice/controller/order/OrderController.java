package com.education.restaurantservice.controller.order;

import com.education.restaurantservice.dto.order.OrderDTO;
import com.education.restaurantservice.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create-table-order/{tableNumber}")
    public ResponseEntity<OrderDTO> createTableOrder(@PathVariable Integer tableNumber) {
        OrderDTO orderDTO = orderService.createTableOrder(tableNumber);
        return ResponseEntity.ok(orderDTO);
    }
}
