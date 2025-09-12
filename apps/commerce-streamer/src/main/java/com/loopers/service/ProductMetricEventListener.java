package com.loopers.service;

import com.loopers.domain.ProductMetricEvent.ProductMetricEventMarker;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
class ProductMetricEventListener {

    private final ProductMetricAggregate productMetricAggregate;

    @Async("metricExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ProductMetricEventMarker event) {
        productMetricAggregate.aggregate(event.getProductId(), event.getType(), event.getScore());
    }
}
