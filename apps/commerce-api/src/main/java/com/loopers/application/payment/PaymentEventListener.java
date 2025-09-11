package com.loopers.application.payment;

import static com.loopers.event.outbox.config.EventAsyncConstant.EVENT;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.domain.order.OrderEvent.OrderCreatedEvent;
import com.loopers.event.outbox.support.EventEnvelope;
import com.loopers.event.outbox.support.SagaTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentEventListener {

    private final PaymentFacade paymentFacade;

    @Async(EVENT)
    @SagaTrace
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(EventEnvelope<OrderCreatedEvent> event) {
        var command = PaymentRequestCommand.from(event.payload());
        paymentFacade.paymentRequest(command);
    }
}
