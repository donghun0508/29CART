package com.loopers.application.payment.processor.support;

import com.loopers.application.payment.processor.PaymentProcessor.PaymentException;
import org.springframework.stereotype.Component;

@Component
class NotEnoughPointExceptionResolver implements ExceptionResolver {

    @Override
    public boolean supports(Exception e) {
        return e.getMessage() != null && e.getMessage().contains("Money.subtract().other");
    }

    @Override
    public PaymentException resolve(Exception e) {
        return new PaymentException("포인트가 부족합니다.", e);
    }
}
