package com.loopers.application.payment.adapter;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.processor.CardPaymentProcessor;
import com.loopers.application.payment.processor.CardPaymentProcessor.CardPaymentRequest;
import com.loopers.domain.payment.CardMethod;
import com.loopers.domain.payment.PaymentClient;
import com.loopers.domain.payment.PaymentClientData.PaymentClientRequest;
import com.loopers.domain.payment.PaymentClientData.PaymentClientResponse;
import com.loopers.domain.payment.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CardPaymentAdapter implements PaymentAdapter {

    private final PaymentClient paymentClient;
    private final CardPaymentProcessor cardPaymentHandler;
    private final CardPaymentConverter cardPaymentConverter;

    @Override
    public boolean supports(PaymentMethod paymentMethod) {
        return paymentMethod instanceof CardMethod;
    }

    @Override
    public void invoke(PaymentRequestCommand command) {
        // 외부 결제 요청
        PaymentClientRequest paymentClientRequest = cardPaymentConverter.convert(command);
        PaymentClientResponse paymentClientResponse = paymentClient.requestPayment(paymentClientRequest);

        CardPaymentRequest cardPaymentData = cardPaymentConverter.convert(command, paymentClientResponse);
        cardPaymentHandler.execute(cardPaymentData);
    }
}
