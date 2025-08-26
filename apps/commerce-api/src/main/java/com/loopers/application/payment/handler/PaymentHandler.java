package com.loopers.application.payment.handler;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.data.PaymentMethod;
import com.loopers.domain.payment.Payment;


public interface PaymentHandler {

    boolean supports(PaymentMethod paymentMethod);

    Payment process(PaymentRequestCommand command);
}
