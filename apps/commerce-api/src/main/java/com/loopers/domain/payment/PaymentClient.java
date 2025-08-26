package com.loopers.domain.payment;

import com.loopers.domain.payment.PaymentClientData.PaymentClientRequest;
import com.loopers.domain.payment.PaymentClientData.PaymentClientResponse;
import com.loopers.domain.payment.PaymentClientData.PaymentClientTransactionDetailResponse;

public interface PaymentClient {

    PaymentClientResponse requestPayment(PaymentClientRequest request);

    PaymentClientTransactionDetailResponse getTransaction(String transactionKey);

}
