package com.loopers.application.payment.handler;

import com.loopers.application.payment.data.PaymentMethod;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentHandlers {

    private final List<PaymentHandler> handlers;

    public PaymentHandler getPaymentHandler(PaymentMethod paymentMethod) {
        return handlers.stream()
            .filter(handler -> handler.supports(paymentMethod))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("지원하지 않는 결제 방식입니다. paymentData: " + paymentMethod.getClass().getSimpleName()));
    }
}
