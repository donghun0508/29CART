package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.TransactionStatus;
import com.loopers.domain.shared.Money;
import lombok.Builder;

public class PaymentCommand {

    public record PaymentRequestCommand(OrderNumber orderNumber, Long buyerId, Money paidAmount, PaymentMethod paymentMethod) {

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
