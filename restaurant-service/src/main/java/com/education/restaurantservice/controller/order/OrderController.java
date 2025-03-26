package com.education.restaurantservice.controller.order;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.restaurantservice.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create order for a table", description = "Create an order for a specified table by its number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully created order for the table",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    @PostMapping("/create-table-order/{tableNumber}")
    public ResponseEntity<OrderDTO> createTableOrder(@PathVariable Integer tableNumber) {
        OrderDTO orderDTO = orderService.createTableOrder(tableNumber);
        return ResponseEntity.ok(orderDTO);
    }

    @Operation(summary = "Retry payment for an order", description = "Send an order for a payment retry by order ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully sent order for payment retry",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PostMapping("/{id}/retry-payment")
    public ResponseEntity<String> retryPayment(@PathVariable Long id) {
        orderService.retryPayment(id);
        return ResponseEntity.ok("Order " + id + " sent for payment retry.");
    }

    @Operation(summary = "Serve order to table", description = "Mark the order as served to a specific table.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully served order to the table",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PostMapping("/{orderId}/serve-to-table")
    public ResponseEntity<OrderDTO> serveOrderToTable(@PathVariable Long orderId) {
        OrderDTO orderDTO = orderService.serveOrderToTable(orderId);
        return ResponseEntity.ok(orderDTO);
    }
}

