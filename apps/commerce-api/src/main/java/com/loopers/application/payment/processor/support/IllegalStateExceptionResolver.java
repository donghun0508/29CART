package com.loopers.application.payment.processor.support;

import com.loopers.application.payment.processor.PaymentProcessor.PaymentException;
import org.springframework.stereotype.Component;

@Component
class IllegalStateExceptionResolver implements ExceptionResolver {

    @Override
    public boolean supports(Exception e) {
        return e instanceof IllegalStateException;
    }

    @Override
    public PaymentException resolve(Exception e) {
        return new PaymentException("결제 상태가 올바르지 않습니다.", e);
    }
}
