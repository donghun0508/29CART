package com.loopers.application.catalog;

import static com.loopers.async.config.AsyncConstant.DEFAULT;

import com.loopers.domain.heart.HeartEvent.HeartCreatedEvent;
import com.loopers.domain.heart.HeartEvent.HeartRemovedEvent;
import com.loopers.domain.heart.TargetType;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.PaymentEvent.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
class ProductEventListener {

    private final ProductFacade productFacade;

    @Async(DEFAULT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentFailedEvent event) {
        productFacade.restore(OrderNumber.of(event.getOrderNumber()));
    }

    @Async(DEFAULT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(HeartCreatedEvent event) {
        if(event.getTargetType() == TargetType.PRODUCT) {
            productFacade.incrementHeart(event.getTargetId());
        }
    }

    @Async(DEFAULT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(HeartRemovedEvent event) {
        if(event.getTargetType() == TargetType.PRODUCT) {
            productFacade.decrementHeart(event.getTargetId());
        }
    }
}
