package com.loopers.application.payment;

import com.loopers.domain.order.OrderEvent.OrderCreatedEvent;
import com.loopers.domain.shared.Money;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.TransactionStatus;
import lombok.Builder;

public class PaymentCommand {

    public record PaymentRequestCommand(OrderNumber orderNumber, Long buyerId, Money paidAmount, PaymentMethod paymentMethod) {

        public static PaymentRequestCommand from(OrderCreatedEvent orderCreatedEvent) {
            return new PaymentRequestCommand(
                OrderNumber.of(orderCreatedEvent.getOrderNumber()),
                orderCreatedEvent.getBuyerId(),
                Money.of(orderCreatedEvent.getPaidAmount()),
                orderCreatedEvent.getPaymentMethod()
            );
        }
    }

    @Builder
    public record PaymentSyncCommand(
        String transactionKey,
        OrderNumber orderNumber,
        CardType cardType,
        String cardNo,
        Long amount,
        TransactionStatus status,
        String reason
    ) {

        public boolean isSuccess() {
            return this.status == TransactionStatus.SUCCESS;
        }
    }

}
