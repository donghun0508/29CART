package com.loopers.infrastructure.client;

import com.loopers.domain.payment.TransactionStatus;

public record TransactionResponse(String transactionKey, TransactionStatus status, String reason) {

}
