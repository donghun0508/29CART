package com.loopers.domain.payment;

import com.loopers.common.domain.DomainEvent;
import com.loopers.domain.shared.Money;
import lombok.Getter;

public class PaymentEvent {

    @Getter
    public static class PaymentCompletedEvent implements DomainEvent {

        private final String orderNumber;
        private final Money paidAmount;

        public PaymentCompletedEvent(Payment payment) {
            this.orderNumber = payment.getOrderNumber();
            this.paidAmount = payment.getPaidAmount();
        }
    }

    @Getter
    public static class CardPaymentRequestedEvent implements DomainEvent {

        private final String orderNumber;
        private final Money paidAmount;
        private final PaymentProvider paymentProvider;

        public CardPaymentRequestedEvent(CardPayment payment) {
            this.orderNumber = payment.getOrderNumber();
            this.paidAmount = payment.getPaidAmount();
            this.paymentProvider = payment.getPaymentProvider();
        }
    }

    @Getter
    public static class PaymentFailedEvent implements DomainEvent {

        private final String orderNumber;
        private final Money paidAmount;
        private final PaymentStatus paymentStatus;
        private final String failureReason;

        public PaymentFailedEvent(Payment payment) {
            this.orderNumber = payment.getOrderNumber();
            this.paidAmount = payment.getPaidAmount();
            this.paymentStatus = payment.getStatus();
            this.failureReason = payment.getFailureReason();
        }
    }
}
