package com.loopers.application.payment.adapter;

import com.loopers.domain.payment.PaymentMethod;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentAdapterRegistry {

    private final List<PaymentAdapter> handlers;

    public PaymentAdapter resolve(PaymentMethod paymentMethod) {
        return handlers.stream()
            .filter(handler -> handler.supports(paymentMethod))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("지원하지 않는 결제 방식입니다. paymentData: " + paymentMethod.getClass().getSimpleName()));
    }
}
