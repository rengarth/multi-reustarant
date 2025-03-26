package com.education.restaurantservice.controller.order;

import com.education.restaurantservice.dto.order.OrderDTO;
import com.education.restaurantservice.service.order.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;


    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = adminOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = adminOrderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/waiter/{id}")
    public ResponseEntity<List<OrderDTO>> getWaiterOrders(@PathVariable Long id) {
        List<OrderDTO> orders = adminOrderService.getWaiterOrders(id);
        return ResponseEntity.ok(orders);
    }

}
