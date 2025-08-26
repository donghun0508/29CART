package com.loopers.infrastructure.client;

import static java.util.Objects.isNull;

import com.loopers.domain.payment.PaymentClientData.PaymentClientRequest;
import com.loopers.domain.payment.PaymentClientData.PaymentClientTransactionDetailResponse;
import org.springframework.stereotype.Component;

@Component
class PaymentRequestConverter {

    public PaymentRequest convert(PaymentClientRequest source) {
        return new PaymentRequest(
            source.orderId(),
            source.cardType(),
            source.cardNo(),
            source.amount(),
            source.callbackUrl()
        );
    }

    public PaymentClientTransactionDetailResponse convert(TransactionDetailResponse source) {
        if (isNull(source)) {
            return null;
        }

        return PaymentClientTransactionDetailResponse.builder()
            .transactionKey(source.transactionKey())
            .orderId(source.orderId())
            .cardType(source.cardType())
            .cardNo(source.cardNo())
            .amount(source.amount())
            .status(source.status())
            .reason(source.reason())
            .build();
    }
}
