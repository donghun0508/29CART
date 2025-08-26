package com.loopers.infrastructure.client;

import com.loopers.domain.payment.TransactionStatus;

public record PaymentResponse(
    String transactionKey,
    TransactionStatus status,
    String reason
) {

    public boolean isSuccess() {
        return status == TransactionStatus.SUCCESS;
    }
}
