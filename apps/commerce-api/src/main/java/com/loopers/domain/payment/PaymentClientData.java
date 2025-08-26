package com.loopers.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import lombok.Builder;

public class PaymentClientData {

    public record PaymentClientRequest(
        String orderId,
        CardType cardType,
        String cardNo,
        Long amount,
        String callbackUrl
    ) {

    }


    @Builder
    public record PaymentClientResponse(String transactionKey, TransactionStatus status, String reason) implements Serializable {

        public static PaymentClientResponse fail(String reason) {
            return PaymentClientResponse.builder()
                .transactionKey(null)
                .status(TransactionStatus.FAILED)
                .reason(reason)
                .build();
        }

        @JsonIgnore
        public boolean isFail() {
            return this.status == TransactionStatus.FAILED;
        }
    }

    @Builder
    public record PaymentClientTransactionDetailResponse(
        String transactionKey,
        String orderId,
        CardType cardType,
        String cardNo,
        Long amount,
        TransactionStatus status,
        String reason
    ) {

    }
}
