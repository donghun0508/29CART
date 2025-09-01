package com.loopers.application.payment.processor.support;

import com.loopers.application.payment.processor.PaymentProcessor.PaymentException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PaymentExceptionTranslator {

    private final List<ExceptionResolver> resolvers;
    private final ExceptionResolver defaultResolver;

    public PaymentExceptionTranslator(List<ExceptionResolver> resolvers) {
        this.resolvers = resolvers;
        this.defaultResolver = new DefaultExceptionResolver();
    }

    public PaymentException translate(Exception e) {
        return resolvers.stream()
            .filter(resolver -> resolver.supports(e))
            .findFirst()
            .map(resolver -> resolver.resolve(e))
            .orElseGet(() -> defaultResolver.resolve(e));
    }
}
