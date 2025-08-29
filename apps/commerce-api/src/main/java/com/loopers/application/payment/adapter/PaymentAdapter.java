package com.loopers.application.payment.adapter;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.domain.payment.PaymentMethod;

public interface PaymentAdapter {
    boolean supports(PaymentMethod paymentMethod);
    void invoke(PaymentRequestCommand command);
}
