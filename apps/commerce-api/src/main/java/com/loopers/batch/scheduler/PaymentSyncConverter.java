package com.loopers.batch.scheduler;

import com.loopers.application.payment.PaymentCommand.PaymentSyncCommand;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.PaymentClientData.PaymentClientTransactionDetailResponse;
import org.springframework.stereotype.Component;

@Component
class PaymentSyncConverter {

    public PaymentSyncCommand convert(PaymentClientTransactionDetailResponse source) {
        return PaymentSyncCommand.builder()
            .transactionKey(source.transactionKey())
            .orderNumber(OrderNumber.of(source.orderId()))
            .cardType(source.cardType())
            .cardNo(source.cardNo())
            .amount(source.amount())
            .status(source.status())
            .reason(source.reason())
            .build();
    }
}
