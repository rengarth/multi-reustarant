package com.education.paymentservice.controller;

import com.education.paymentservice.dto.PaymentTransactionDTO;
import com.education.paymentservice.service.PaymentTransactionService;
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

    @GetMapping
    public ResponseEntity<List<PaymentTransactionDTO>> getAllTransactions() {
        List<PaymentTransactionDTO> transactions = paymentTransactionService.getAllPaymentTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentTransactionDTO>> getPaymentTransactionsByOrderId(
            @PathVariable("orderId") Long orderId) {
        List<PaymentTransactionDTO> transactions = paymentTransactionService
                .getPaymentTransactionsByOrderId(orderId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTransactionDTO> getPaymentTransactionById(@PathVariable("id") Long id) {
        PaymentTransactionDTO transaction = paymentTransactionService.getPaymentTransactionById(id);
        return ResponseEntity.ok(transaction);
    }
}
