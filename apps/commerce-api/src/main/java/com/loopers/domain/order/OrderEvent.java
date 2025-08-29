package com.loopers.domain.order;


import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.shared.DomainEvent;
import com.loopers.domain.shared.Money;
import com.loopers.logging.support.alert.NotificationEvent;
import lombok.Getter;

public class OrderEvent {

    @Getter
    public static class OrderCreatedEvent extends DomainEvent implements NotificationEvent {

        private final Long buyerId;
        private final Money paidAmount;
        private final OrderNumber orderNumber;
        private final PaymentMethod paymentMethod;

        public OrderCreatedEvent(Order order) {
            this.buyerId = order.getBuyerId();
            this.paidAmount = order.paidAmount();
            this.orderNumber = order.getOrderNumber();
            this.paymentMethod = order.getPaymentMethod();
        }

        @Override
        public String message() {
            return String.format("주문이 생성되었습니다. [주문번호: %s] [고객: %d] [결제 금액: %s] [결제수단: %s]",
                orderNumber,
                buyerId,
                paidAmount,
                paymentMethod.getClass().getSimpleName());
        }
    }

    public static class OrderCompletedEvent extends DomainEvent implements NotificationEvent {

        private final Long buyerId;
        private final Money paidAmount;
        private final OrderNumber orderNumber;

        public OrderCompletedEvent(Order order) {
            this.buyerId = order.getBuyerId();
            this.paidAmount = order.paidAmount();
            this.orderNumber = order.getOrderNumber();
        }

        @Override
        public String message() {
            return String.format("주문이 성공적으로 완료되었습니다. [주문번호: %s] [결제 금액: %s]", orderNumber, paidAmount);
        }
    }


    @Getter
    public static class OrderFailedEvent extends DomainEvent implements NotificationEvent {

        private final Long buyerId;
        private final Money paidAmount;
        private final OrderNumber orderNumber;
        private final Long issuedCouponId;
        private final boolean isCouponUsed;

        public OrderFailedEvent(Order order) {
            this.buyerId = order.getBuyerId();
            this.paidAmount = order.paidAmount();
            this.orderNumber = order.getOrderNumber();
            this.issuedCouponId = order.issuedCouponId();
            this.isCouponUsed = issuedCouponId != null;
        }

        @Override
        public String message() {
            return String.format("주문이 실패되었습니다. [주문번호: %s] [결제 금액: %s]", orderNumber, paidAmount);
        }
    }

    @Getter
    public static class OrderAppliedCouponEvent extends  DomainEvent implements NotificationEvent {

        private final String orderNumber;
        private final Long issuedCouponId;
        private final Money totalAmount;
        private final Money paidAmount;

        public OrderAppliedCouponEvent(Order order) {
            this.orderNumber = order.getOrderNumber().toString();
            this.issuedCouponId = order.issuedCouponId();
            this.totalAmount = order.getTotalAmount();
            this.paidAmount = order.paidAmount();
        }

        @Override
        public String message() {
            return String.format("쿠폰이 적용되었습니다. [주문번호: %s] [쿠폰: %d] [할인 전 금액: %s] [할인 후 금액: %s]", orderNumber, issuedCouponId,
                totalAmount, paidAmount);
        }
    }

}
