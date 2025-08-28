package com.loopers.domain.payment;

import com.loopers.domain.shared.DomainEvent;
import com.loopers.domain.shared.Money;
import com.loopers.logging.support.alert.NotificationEvent;
import lombok.Getter;

public class PaymentEvent {

    @Getter
    public static class PaymentCompletedEvent extends DomainEvent implements NotificationEvent {

        private final String orderNumber;
        private final Money paidAmount;

        public PaymentCompletedEvent(Payment payment) {
            this.orderNumber = payment.getOrderNumber();
            this.paidAmount = payment.getPaidAmount();
        }

        @Override
        public String message() {
            return String.format("결제가 완료되었습니다. [주문번호: %s] [결제 금액: %s원]", orderNumber, paidAmount);
        }
    }

    @Getter
    public static class CardPaymentRequestedEvent extends DomainEvent implements NotificationEvent{

        private final String orderNumber;
        private final Money paidAmount;
        private final PaymentProvider paymentProvider;

        public CardPaymentRequestedEvent(CardPayment payment) {
            this.orderNumber = payment.getOrderNumber();
            this.paidAmount = payment.getPaidAmount();
            this.paymentProvider = payment.getPaymentProvider();
        }

        @Override
        public String message() {
            return String.format("카드 결제 요청이 처리되었습니다. [주문번호: %s] [결제 금액: %s원] [결제사: %s]", orderNumber, paidAmount, paymentProvider);
        }
    }


    @Getter
    public static class PaymentFailedEvent extends DomainEvent implements NotificationEvent {

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

        @Override
        public String message() {
            return String.format("결제가 실패했습니다. [주문번호: %s] [결제 금액: %s원] [상태: %s] [실패사유: %s]",
                orderNumber, paidAmount, paymentStatus, failureReason);
        }

    }
}
