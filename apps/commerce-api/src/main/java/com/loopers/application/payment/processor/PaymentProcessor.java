package com.loopers.application.payment.processor;

import com.loopers.application.payment.processor.PaymentProcessor.PaymentRequest;
import com.loopers.domain.order.OrderNumber;

@FunctionalInterface
public interface PaymentProcessor<T extends PaymentRequest> {

    void execute(T command);

    interface PaymentRequest {

        OrderNumber orderNumber();
    }

    class PaymentException extends RuntimeException {
        private final String reason;

        public PaymentException(String reason) {
            this.reason = reason;
        }

        public PaymentException(String reason, Throwable cause) {
            super(cause);
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }
    }
}
