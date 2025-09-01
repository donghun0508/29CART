package com.loopers.application.order;

import static com.loopers.async.config.AsyncConstant.DEFAULT;

import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.PaymentEvent.PaymentCompletedEvent;
import com.loopers.domain.payment.PaymentEvent.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
class OrderEventListener {

    private final OrderFacade orderFacade;

    @Async(DEFAULT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentCompletedEvent event) {
        orderFacade.complete(OrderNumber.of(event.getOrderNumber()));
    }

    @Async(DEFAULT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentFailedEvent event) {
        orderFacade.fail(OrderNumber.of(event.getOrderNumber()));
    }
}
