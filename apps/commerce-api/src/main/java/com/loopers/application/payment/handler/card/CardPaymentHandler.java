package com.loopers.application.payment.handler.card;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.data.CardMethod;
import com.loopers.application.payment.data.PaymentMethod;
import com.loopers.application.payment.handler.PaymentHandler;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class CardPaymentHandler implements PaymentHandler {

    private final PaymentGateways paymentGateways;
    private final PaymentGatewayRouter paymentGatewayRouter;

    @Override
    public boolean supports(PaymentMethod paymentMethod) {
        return paymentMethod instanceof CardMethod;
    }

    @Override
    public Payment process(PaymentRequestCommand command) {
        PaymentProvider paymentProvider = paymentGatewayRouter.defaultPaymentProvider();
        PaymentGateway paymentGateway = paymentGateways.getPaymentGatewayStrategy(paymentProvider);
        return paymentGateway.processPayment(command);
    }
}
