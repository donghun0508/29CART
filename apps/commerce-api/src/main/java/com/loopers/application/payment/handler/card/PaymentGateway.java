package com.loopers.application.payment.handler.card;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentProvider;

public interface PaymentGateway {

    boolean supports(PaymentProvider selectedPg);

    Payment processPayment(PaymentRequestCommand command);
}
