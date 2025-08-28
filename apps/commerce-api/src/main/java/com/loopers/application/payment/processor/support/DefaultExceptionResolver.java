package com.loopers.application.payment.processor.support;

import com.loopers.application.payment.processor.PaymentProcessor.PaymentException;

class DefaultExceptionResolver implements ExceptionResolver {

    @Override
    public boolean supports(Exception e) {
        return true;
    }

    @Override
    public PaymentException resolve(Exception e) {
        return new PaymentException("알 수 없는 오류로 결제에 실패했습니다.", e);
    }
}
