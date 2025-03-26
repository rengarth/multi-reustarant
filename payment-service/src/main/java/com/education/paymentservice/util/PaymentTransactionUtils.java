package com.education.paymentservice.util;

import com.education.paymentservice.dto.PaymentTransactionDTO;
import com.education.paymentservice.entity.PaymentTransaction;

public class PaymentTransactionUtils {

    public static PaymentTransactionDTO convertTransactionToDTO(PaymentTransaction transaction) {
        PaymentTransactionDTO transactionDTO = new PaymentTransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setOrderId(transaction.getOrderId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTransactionStatus(transaction.getTransactionStatus());
        transactionDTO.setTransactionTime(transaction.getTransactionTime());
        return transactionDTO;
    }
}
