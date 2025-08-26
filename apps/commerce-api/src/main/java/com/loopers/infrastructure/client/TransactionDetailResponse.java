package com.loopers.infrastructure.client;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.TransactionStatus;

public record TransactionDetailResponse(
    String transactionKey,
    String orderId,
    CardType cardType,
    String cardNo,
    Long amount,
    TransactionStatus status,
    String reason
) {

}
