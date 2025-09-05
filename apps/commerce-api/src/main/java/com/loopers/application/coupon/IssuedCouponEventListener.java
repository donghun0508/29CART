package com.loopers.application.coupon;

import static com.loopers.event.outbox.config.EventAsyncConstant.EVENT;

import com.loopers.domain.order.OrderEvent.OrderCreatedEvent;
import com.loopers.domain.order.OrderEvent.OrderFailedEvent;
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
public class IssuedCouponEventListener {

    private final IssuedCouponFacade issuedCouponFacade;

    @Async(EVENT)
    @SagaTrace
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreatedEvent(EventEnvelope<OrderCreatedEvent> event) {
        OrderCreatedEvent payload = event.payload();
        if (payload.getIssuedCouponId() != null) {
            issuedCouponFacade.use(payload.getIssuedCouponId());
        }
    }

    @Async(EVENT)
    @SagaTrace
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderFailedEvent(EventEnvelope<OrderFailedEvent> event) {
        OrderFailedEvent payload = event.payload();
        if (payload.isCouponUsed()) {
            issuedCouponFacade.cancel(payload.getIssuedCouponId());
        }
    }
}
