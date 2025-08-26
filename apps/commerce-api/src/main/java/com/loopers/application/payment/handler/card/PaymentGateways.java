package com.loopers.application.payment.handler.card;

import com.loopers.domain.payment.PaymentProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentGateways {

    private final List<PaymentGateway> gateways;

    public PaymentGateway getPaymentGatewayStrategy(PaymentProvider paymentProvider) {
        return gateways.stream()
            .filter(s -> s.supports(paymentProvider))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "지원하지 않는 결제 방식입니다. paymentProvider: " + paymentProvider.name()));
    }
}
