package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCommand.PaymentSyncCommand;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.TransactionStatus;

public class PaymentV1Dto {

    public record TransactionCallback(
        String transactionKey,
        String orderId,
        CardType cardType,
        String cardNo,
        Long amount,
        TransactionCallbackStatus status,
        String reason
    ) {

        public PaymentSyncCommand toCommand() {
            return new PaymentSyncCommand(
                transactionKey,
                new OrderNumber(orderId),
                cardType,
                cardNo,
                amount,
                TransactionStatus.valueOf(status.name()),
                reason
            );
        }

        public enum TransactionCallbackStatus {
            PENDING,
            SUCCESS,
            FAILED
        }
    }
}
