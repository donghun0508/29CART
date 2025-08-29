package com.loopers.infrastructure.client.payment;


import com.loopers.domain.payment.CardType;

public record PaymentRequest(
    String orderId,
    CardType cardType,
    String cardNo,
    Long amount,
    String callbackUrl
) {

}
