package com.loopers.domain.order;


import com.loopers.common.domain.DomainEvent;
import com.loopers.domain.shared.Money;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.shared.OrderCoupon;
import java.util.Map;
import lombok.Getter;

public class OrderEvent {

    @Getter
    public static class OrderCreatedEvent implements DomainEvent {

        private final Long buyerId;
        private final String status;
        private final Map<Long, Long> orderLines;
        private final Long totalAmount;
        private final Long paidAmount;
        private final Long issuedCouponId;
        private final String idempotencyKey;
        private final String orderNumber;
        private final PaymentMethod paymentMethod;

        public OrderCreatedEvent(Order order) {
            this.buyerId = order.getBuyerId();
            this.status = order.getStatus().name();
            this.orderLines = order.purchaseProducts();
            this.totalAmount = order.getTotalAmount().value();
            this.paidAmount = order.paidAmount().value();
            this.issuedCouponId = order.getOrderCoupon().issuedCouponId();
            this.idempotencyKey = order.getIdempotencyKey().key();
            this.orderNumber = order.getOrderNumber().number();
            this.paymentMethod = order.getPaymentMethod();
        }
    }

    @Getter
    public static class OrderCompletedEvent implements DomainEvent {

        private final Long buyerId;
        private final Money paidAmount;
        private final OrderNumber orderNumber;

        public OrderCompletedEvent(Order order) {
            this.buyerId = order.getBuyerId();
            this.paidAmount = order.paidAmount();
            this.orderNumber = order.getOrderNumber();
        }
    }


    @Getter
    public static class OrderFailedEvent implements DomainEvent {

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
    }

}
