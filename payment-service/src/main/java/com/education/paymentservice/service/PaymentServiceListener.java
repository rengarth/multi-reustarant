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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceListener {

    private final PaymentTransactionService paymentTransactionService;
    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
            log.info("Order {} payment successful. Waiting 3 seconds before sending to Kafka...", orderDTO.getId());

            // Отправляем сообщение в Kafka с задержкой 3 секунды
            scheduler.schedule(() -> {
                kafkaTemplate.send("order-updates", orderDTO);
                kafkaTemplate.send("order-cooking", orderDTO);
                log.info("Order {} sent to kitchen.", orderDTO.getId());
            }, 3, TimeUnit.SECONDS);
        }
        else {
            log.error("Order statuses is incorrect. Payment aborted for order id: {}", orderDTO.getId());
            paymentTransactionService.createPaymentTransaction(orderDTO, TransactionStatus.FAILED);
        }
    }
}

