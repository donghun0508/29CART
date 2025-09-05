package com.loopers.application.catalog;

import static com.loopers.event.outbox.config.EventAsyncConstant.EVENT;

import com.loopers.common.domain.DomainEvent;
import com.loopers.domain.catalog.ProductEvent.ProductDetailViewedEvent;
import com.loopers.domain.heart.HeartEvent.HeartCreatedEvent;
import com.loopers.domain.heart.HeartEvent.HeartRemovedEvent;
import com.loopers.domain.heart.TargetType;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.PaymentEvent.PaymentFailedEvent;
import com.loopers.event.outbox.EventEnvelopeFactory;
import com.loopers.event.outbox.service.Producer;
import com.loopers.event.outbox.support.EventEnvelope;
import com.loopers.event.outbox.support.EventEnvelope.EventMetadata;
import com.loopers.event.outbox.support.SagaTrace;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
class ProductEventListener {

    private final Producer producer;
    private final EventEnvelopeFactory eventEnvelopeFactory;
    private final ProductFacade productFacade;

    @Async(EVENT)
    @SagaTrace
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentFailedEvent(EventEnvelope<PaymentFailedEvent> event) {
        PaymentFailedEvent payload = event.payload();
        productFacade.restore(OrderNumber.of(payload.getOrderNumber()));
    }

    @Async(EVENT)
    @SagaTrace
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleHeartCreatedEvent(EventEnvelope<HeartCreatedEvent> event) {
        HeartCreatedEvent payload = event.payload();
        if (payload.getTargetType() == TargetType.PRODUCT) {
            productFacade.incrementHeart(payload.getTargetId());
        }
    }

    @Async(EVENT)
    @SagaTrace
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleHeartRemovedEvent(EventEnvelope<HeartRemovedEvent> event) {
        HeartRemovedEvent payload = event.payload();
        if (payload.getTargetType() == TargetType.PRODUCT) {
            productFacade.decrementHeart(payload.getTargetId());
        }
    }

    @Async(EVENT)
    @EventListener(ProductDetailViewedEvent.class)
    public void handleProductDetailViewedEvent(ProductDetailViewedEvent event) {
        EventEnvelope<ProductDetailViewedEvent> envelope = createEventEnvelope(event);
        producer.send("catalog-events", envelope);
    }

    private EventEnvelope<ProductDetailViewedEvent> createEventEnvelope(ProductDetailViewedEvent event) {
        var md = new EventMetadata(
            UUID.randomUUID().toString(),
            event.getClass().getSimpleName(),
            1,
            null,
            null,
            event.getClass().getSimpleName(),
            event.getProductId().toString(),
            Instant.now(Clock.systemUTC()),
            "catalog-service"
        );
        return new EventEnvelope<>(md, event);
    }
}
