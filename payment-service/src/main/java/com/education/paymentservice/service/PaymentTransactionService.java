package com.education.paymentservice.service;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.paymentservice.dto.PaymentTransactionDTO;
import com.education.paymentservice.entity.PaymentTransaction;
import com.education.paymentservice.entity.TransactionStatus;
import com.education.paymentservice.repository.PaymentTransactionRepository;
import com.education.paymentservice.util.PaymentTransactionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionDTO createPaymentTransaction(OrderDTO orderDTO, TransactionStatus status) {
        log.info("Creating payment transaction for order id: {}, amount: {}, status: {}", orderDTO.getId(), orderDTO.getTotalAmount(), status);

        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setOrderId(orderDTO.getId());
        paymentTransaction.setAmount(orderDTO.getTotalAmount());
        paymentTransaction.setTransactionStatus(status);
        paymentTransaction.setTransactionTime(LocalDateTime.now());
        paymentTransactionRepository.save(paymentTransaction);

        log.info("Payment transaction created for order id: {}", orderDTO.getId());
        return PaymentTransactionUtils.convertTransactionToDTO(paymentTransaction);
    }

    public List<PaymentTransactionDTO> getPaymentTransactionsByOrderId(Long orderId) {
        log.info("Fetching payment transactions for order id: {}...", orderId);

        List<PaymentTransaction> transactions = paymentTransactionRepository.findPaymentTransactionsByOrderId(orderId);
        log.info("Found {} payment transactions for order id: {}", transactions.size(), orderId);

        return transactions.stream().map(PaymentTransactionUtils::convertTransactionToDTO).toList();
    }

    public List<PaymentTransactionDTO> getAllPaymentTransactions() {
        log.info("Fetching all payment transactions...");

        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();
        log.info("Found {} total payment transactions", transactions.size());

        return transactions.stream().map(PaymentTransactionUtils::convertTransactionToDTO).toList();
    }

    public PaymentTransactionDTO getPaymentTransactionById(Long id) {
        log.info("Fetching payment transaction with id: {}...", id);

        PaymentTransaction paymentTransaction = paymentTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment transaction not found"));

        log.info("Payment transaction found with id: {}", id);
        return PaymentTransactionUtils.convertTransactionToDTO(paymentTransaction);
    }
}

