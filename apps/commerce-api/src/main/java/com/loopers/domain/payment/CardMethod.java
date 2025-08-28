package com.loopers.domain.payment;

public record CardMethod(CardType cardType, CardNumber cardNumber) implements PaymentMethod {

}
