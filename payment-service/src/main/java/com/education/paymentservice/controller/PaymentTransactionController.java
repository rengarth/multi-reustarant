package com.education.paymentservice.controller;

import com.education.paymentservice.dto.PaymentTransactionDTO;
import com.education.paymentservice.service.PaymentTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @Operation(summary = "Get all payment transactions",
            description = "Retrieve a list of all payment transactions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all payment transactions",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = PaymentTransactionDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PaymentTransactionDTO>> getAllTransactions() {
        List<PaymentTransactionDTO> transactions = paymentTransactionService.getAllPaymentTransactions();
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get payment transactions by order ID",
            description = "Retrieve a list of payment transactions for a specific order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved payment transactions for the order",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = PaymentTransactionDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentTransactionDTO>> getPaymentTransactionsByOrderId(
            @PathVariable("orderId") Long orderId) {
        List<PaymentTransactionDTO> transactions = paymentTransactionService
                .getPaymentTransactionsByOrderId(orderId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get payment transaction by ID",
            description = "Retrieve a specific payment transaction by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the payment transaction",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentTransactionDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Payment transaction not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentTransactionDTO> getPaymentTransactionById(@PathVariable("id") Long id) {
        PaymentTransactionDTO transaction = paymentTransactionService.getPaymentTransactionById(id);
        return ResponseEntity.ok(transaction);
    }
}

