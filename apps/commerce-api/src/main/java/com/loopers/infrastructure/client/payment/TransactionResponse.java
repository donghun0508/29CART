package com.loopers.infrastructure.client.payment;

import com.loopers.domain.payment.TransactionStatus;

public record TransactionResponse(String transactionKey, TransactionStatus status, String reason) {

}
