package com.education.kitchen.service;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.kafkadto.dto.order.OrderStatus;
import com.education.kafkadto.dto.order.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class KitchenServiceListener {

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @KafkaListener(topics = "order-cooking", groupId = "kitchen-group")
    public void processOrderForCooking(OrderDTO orderDTO) {
        log.info("Kitchen service received order for cooking: {}", orderDTO);

        if (orderDTO.getPaymentStatus() == PaymentStatus.PAID &&
                orderDTO.getStatus() == OrderStatus.SENT_TO_KITCHEN &&
                orderDTO.getOrderDetails() != null && !orderDTO.getOrderDetails().isEmpty()) {

            orderDTO.setStatus(OrderStatus.COOKING);
            kafkaTemplate.send("order-updates", orderDTO);
            log.info("Order {} status updated to COOKING", orderDTO.getId());

            scheduler.schedule(() -> {
                orderDTO.setStatus(OrderStatus.READY);
                orderDTO.setLeadTime(LocalDateTime.now());
                kafkaTemplate.send("order-updates", orderDTO);
                log.info("Order {} status updated to READY", orderDTO.getId());
            }, 30, TimeUnit.SECONDS);

        } else {
            log.warn("Order {} does not meet the criteria for cooking. Current status: {}, payment status: {}",
                    orderDTO.getId(), orderDTO.getStatus(), orderDTO.getPaymentStatus());
        }
    }
}


