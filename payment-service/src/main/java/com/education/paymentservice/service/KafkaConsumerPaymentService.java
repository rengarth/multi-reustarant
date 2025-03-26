package com.education.paymentservice.service;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.kafkadto.dto.order.OrderStatus;
import com.education.kafkadto.dto.order.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerPaymentService {

    private final PaymentTransactionService paymentTransactionService;

    @KafkaListener(topics = "order-ready-for-payment", groupId = "payment-group")
    public void processPayment(OrderDTO orderDTO) {
        log.info("Received order for payment: {}", orderDTO);

        if (orderDTO.getTotalAmount() <= 0) {
            log.error("Order total amount is 0 or negative. Payment aborted for order id: {}", orderDTO.getId());
        }
        else if (orderDTO.getStatus().equals(OrderStatus.CREATED)
                && orderDTO.getPaymentStatus().equals(PaymentStatus.NOT_PAID)){
            paymentTransactionService.createPaymentTransaction(orderDTO);}
    }
}

