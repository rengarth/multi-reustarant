package com.education.paymentservice.dto;

import com.education.paymentservice.entity.TransactionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentTransactionDTO {
    private Long id;
    private Long orderId;
    private Integer amount;
    private TransactionStatus transactionStatus;
    private LocalDateTime transactionTime;
}
