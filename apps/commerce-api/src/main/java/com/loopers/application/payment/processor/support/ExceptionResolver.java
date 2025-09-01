package com.loopers.application.payment.processor.support;

import com.loopers.application.payment.processor.PaymentProcessor.PaymentException;

public interface ExceptionResolver {

    boolean supports(Exception e);

    PaymentException resolve(Exception e);
}
