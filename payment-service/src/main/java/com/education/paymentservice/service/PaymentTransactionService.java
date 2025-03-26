package com.education.paymentservice.service;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.paymentservice.dto.PaymentTransactionDTO;
import com.education.paymentservice.entity.PaymentTransaction;
import com.education.paymentservice.entity.TransactionStatus;
import com.education.paymentservice.repository.PaymentTransactionRepository;
import com.education.paymentservice.util.PaymentTransactionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionDTO createPaymentTransaction(OrderDTO orderDTO) {
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setOrderId(orderDTO.getId());
        paymentTransaction.setAmount(orderDTO.getTotalAmount());
        paymentTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
        paymentTransaction.setTransactionTime(LocalDateTime.now());
        paymentTransactionRepository.save(paymentTransaction);
        return PaymentTransactionUtils.convertTransactionToDTO(paymentTransaction);
    }

    public List<PaymentTransactionDTO> getPaymentTransactionsByOrderId(Long orderId) {
        List<PaymentTransaction> transactions = paymentTransactionRepository
                .findPaymentTransactionsByOrderId(orderId);
        return transactions.stream().map(PaymentTransactionUtils::convertTransactionToDTO).toList();
    }

    public List<PaymentTransactionDTO> getAllPaymentTransactions() {
        return paymentTransactionRepository
                .findAll()
                .stream()
                .map(PaymentTransactionUtils::convertTransactionToDTO)
                .toList();
    }

    public PaymentTransactionDTO getPaymentTransactionById(Long id) {
        return PaymentTransactionUtils.convertTransactionToDTO(
                paymentTransactionRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Payment transaction not found")));
    }
}
