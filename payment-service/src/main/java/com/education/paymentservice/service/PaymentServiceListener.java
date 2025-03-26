package com.education.paymentservice.service;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.kafkadto.dto.order.OrderStatus;
import com.education.kafkadto.dto.order.PaymentStatus;
import com.education.paymentservice.entity.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceListener {

    private final PaymentTransactionService paymentTransactionService;
    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @KafkaListener(topics = "order-ready-for-payment", groupId = "payment-group")
    public void processPayment(OrderDTO orderDTO) {
        log.info("Received order for payment: {}", orderDTO);

        if (orderDTO.getTotalAmount() <= 0) {
            log.error("Order total amount is 0 or negative. Payment aborted for order id: {}", orderDTO.getId());
            paymentTransactionService.createPaymentTransaction(orderDTO, TransactionStatus.FAILED);
        }
        else if (orderDTO.getStatus().equals(OrderStatus.CREATED)
                && orderDTO.getPaymentStatus().equals(PaymentStatus.NOT_PAID)){
            paymentTransactionService.createPaymentTransaction(orderDTO, TransactionStatus.SUCCESS);
            orderDTO.setPaymentStatus(PaymentStatus.PAID);
            orderDTO.setStatus(OrderStatus.SENT_TO_KITCHEN);
            kafkaTemplate.send("order-status-updates", orderDTO);
            log.info("Order {} payment successful. Sent to kitchen.", orderDTO.getId());
        }

        else {
            log.error("Order statuses is incorrect. Payment aborted for order id: {}", orderDTO.getId());
            paymentTransactionService.createPaymentTransaction(orderDTO, TransactionStatus.FAILED);
        }
    }
}

