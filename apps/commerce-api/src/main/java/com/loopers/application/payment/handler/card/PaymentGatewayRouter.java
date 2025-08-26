package com.loopers.application.payment.handler.card;

import com.loopers.domain.payment.PaymentProvider;
import org.springframework.stereotype.Component;

@Component
class PaymentGatewayRouter {

    public PaymentProvider defaultPaymentProvider() {
        return PaymentProvider.SIMULATOR;
    }
}
