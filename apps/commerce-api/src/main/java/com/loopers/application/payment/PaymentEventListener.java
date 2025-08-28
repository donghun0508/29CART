package com.loopers.application.payment;

import static com.loopers.async.config.AsyncConstant.DEFAULT;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.domain.order.OrderEvent.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
class PaymentEventListener {

    private final PaymentFacade paymentFacade;

    @Async(DEFAULT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCreatedEvent event) {
        PaymentRequestCommand command = new PaymentRequestCommand(event.getOrderNumber(), event.getBuyerId(), event.getPaidAmount(), event.getPaymentMethod());
        paymentFacade.paymentRequest(command);
    }
}
