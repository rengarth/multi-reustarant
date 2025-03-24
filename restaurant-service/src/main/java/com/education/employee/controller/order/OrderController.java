package com.education.employee.controller.order;

import com.education.employee.dto.order.OrderDTO;
import com.education.employee.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create/{tableNumber}")
    public ResponseEntity<OrderDTO> createTableOrder(@PathVariable Integer tableNumber) {
        OrderDTO orderDTO = orderService.createTableOrder(tableNumber);
        return ResponseEntity.ok(orderDTO);
    }
}
