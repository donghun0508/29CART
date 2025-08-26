package com.loopers.application.payment.data;

import com.loopers.domain.payment.CardNumber;
import com.loopers.domain.payment.CardType;

public record CardMethod(CardType cardType, CardNumber cardNumber) implements PaymentMethod {

}
