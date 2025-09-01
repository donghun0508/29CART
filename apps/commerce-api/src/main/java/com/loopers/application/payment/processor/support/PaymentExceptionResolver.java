package com.loopers.application.payment.processor.support;

import com.loopers.application.payment.processor.PaymentProcessor;
import com.loopers.application.payment.processor.PaymentProcessor.PaymentException;
import org.springframework.stereotype.Component;

@Component
class PaymentExceptionResolver implements ExceptionResolver {

    @Override
    public boolean supports(Exception e) {
        return e instanceof PaymentProcessor.PaymentException;
    }

    @Override
    public PaymentException resolve(Exception e) {
        return (PaymentException) e;
    }
}
