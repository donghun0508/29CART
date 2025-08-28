package com.loopers.application.payment.processor.support;

import com.loopers.application.payment.processor.PaymentProcessor.PaymentException;
import org.springframework.stereotype.Component;

@Component
class IllegalArgumentExceptionResolver implements ExceptionResolver {

    @Override
    public boolean supports(Exception e) {
        return e instanceof IllegalArgumentException;
    }

    @Override
    public PaymentException resolve(Exception e) {
        return new PaymentException("잘못된 결제 정보입니다.", e);
    }
}
