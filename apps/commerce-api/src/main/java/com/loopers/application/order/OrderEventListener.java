package com.loopers.application.order;

import static com.loopers.event.outbox.config.EventAsyncConstant.EVENT;

import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.PaymentEvent.PaymentCompletedEvent;
import com.loopers.domain.payment.PaymentEvent.PaymentFailedEvent;
import com.loopers.event.outbox.support.EventEnvelope;
import com.loopers.event.outbox.support.SagaTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
class OrderEventListener {

    private final OrderFacade orderFacade;

    @Async(EVENT)
    @SagaTrace
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompletedEvent(EventEnvelope<PaymentCompletedEvent> event) {
        PaymentCompletedEvent payload = event.payload();
        orderFacade.complete(OrderNumber.of(payload.getOrderNumber()));
    }

    @Async(EVENT)
    @SagaTrace
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentFailedEvent(EventEnvelope<PaymentFailedEvent> event) {
        PaymentFailedEvent payload = event.payload();
        orderFacade.fail(OrderNumber.of(payload.getOrderNumber()));
    }
}
