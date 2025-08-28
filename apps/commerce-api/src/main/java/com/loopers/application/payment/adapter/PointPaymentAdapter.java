package com.loopers.application.payment.adapter;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.processor.PointPaymentProcessor;
import com.loopers.application.payment.processor.PointPaymentProcessor.PointPaymentRequest;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PointMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class PointPaymentAdapter implements PaymentAdapter {

    private final PointPaymentProcessor pointPaymentHandler;

    @Override
    public boolean supports(PaymentMethod paymentMethod) {
        return paymentMethod instanceof PointMethod;
    }

    @Override
    public void invoke(PaymentRequestCommand command) {
        PointPaymentRequest paymentData = new PointPaymentRequest(command.orderNumber(), command.buyerId(), command.paidAmount());
        pointPaymentHandler.execute(paymentData);
    }
}
