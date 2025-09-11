package com.loopers.service.strategy.metric;

import com.loopers.domain.ProductMetric;
import com.loopers.service.ProductMetricAnalysis;
import com.loopers.service.strategy.metric.ProductMetricStrategy.ProductMetricEvent;
import java.util.List;

public interface ProductMetricStrategy<T extends ProductMetricEvent> {
    boolean supports(ProductMetricAnalysis productMetricAnalysis);
    T getProductId(String payload);
    void process(T event, ProductMetric productMetric);

    interface ProductMetricEvent {
        List<Long> productIds();
    }
}
