package com.loopers.domain.payment;

import com.loopers.domain.shared.DomainEvent;

public class PaymentEvent {

    public record CardPaymentCreatedEvent(CardPayment cardPayment) implements DomainEvent {

    }

    public record PointPaymentCreatedEvent(PointPayment pointPayment) implements DomainEvent {

    }

}
