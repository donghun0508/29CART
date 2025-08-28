package com.loopers.application.payment.adapter;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.processor.CardPaymentProcessor.CardPaymentRequest;
import com.loopers.domain.payment.CardMethod;
import com.loopers.domain.payment.PaymentClientData.PaymentClientRequest;
import com.loopers.domain.payment.PaymentClientData.PaymentClientResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class CardPaymentConverter {

    @Value("${external.payment.providers.simulator.callback-url}")
    private String callBackUrl;

    public PaymentClientRequest convert(PaymentRequestCommand command) {
        CardMethod cardData = (CardMethod) command.paymentMethod();
        return new PaymentClientRequest(command.orderNumber().number(), cardData.cardType(), cardData.cardNumber().number(),
            command.paidAmount().value(), callBackUrl);
    }

    public CardPaymentRequest convert(PaymentRequestCommand command, PaymentClientResponse paymentClientResponse) {
        CardMethod cardData = (CardMethod) command.paymentMethod();
        return new CardPaymentRequest(command.orderNumber(), command.paidAmount(), cardData.cardType(),
            paymentClientResponse);
    }
}
