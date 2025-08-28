package com.loopers.application.payment;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.PaymentCommand.PaymentSyncCommand;
import com.loopers.application.payment.adapter.PaymentAdapter;
import com.loopers.application.payment.adapter.PaymentAdapterRegistry;
import com.loopers.domain.payment.CardPayment;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.shared.DomainEventPublisher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PaymentFacade {

    private final PaymentService paymentService;
    private final DomainEventPublisher domainEventPublisher;
    private final PaymentAdapterRegistry paymentAdapterRegistry;

    public void paymentRequest(PaymentRequestCommand command) {
        PaymentAdapter paymentAdapter = paymentAdapterRegistry.resolve(command.paymentMethod());
        paymentAdapter.invoke(command);
    }

    @Transactional
    public void sync(PaymentSyncCommand command) {
        Payment payment = paymentService.findByOrderNumber(command.orderNumber());

        if (command.isSuccess()) {
            payment.complete();
        } else {
            payment.fail(command.reason());
        }
        domainEventPublisher.publishEvent(payment.events());
    }

    public List<String> getSyncTransactionIds() {
        return paymentService.findAllByStatus(PaymentStatus.REQUESTED)
            .stream()
            .filter(CardPayment.class::isInstance)
            .map(CardPayment.class::cast)
            .map(CardPayment::getTransactionId)
            .toList();
    }
}
