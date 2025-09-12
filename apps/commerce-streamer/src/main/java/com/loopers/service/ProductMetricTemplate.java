package com.loopers.service;

import com.loopers.common.domain.DomainEvent;
import com.loopers.domain.ProductMetric;
import com.loopers.repository.ProductMetricRepository;
import com.loopers.service.strategy.metric.ProductMetricStrategy;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductMetricTemplate {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ProductMetricRepository productMetricRepository;
    private final List<ProductMetricStrategy> strategies;

    @Transactional
    public void save(ProductMetricAnalysis productMetricAnalysis) {
        List<DomainEvent> events = new ArrayList<>();
        for (ProductMetricStrategy<?> strategy : strategies) {
            if (strategy.supports(productMetricAnalysis)) {
                ProductMetricStrategy<ProductMetricStrategy.ProductMetricEvent> metricStrategy = (ProductMetricStrategy<ProductMetricStrategy.ProductMetricEvent>) strategy;
                ProductMetricStrategy.ProductMetricEvent productMetricEvent = metricStrategy.getProductId(
                    productMetricAnalysis.getPayload());

                ProductMetric productMetric = productMetricRepository.findByProductId(productMetricEvent.productId())
                    .orElseGet(() -> productMetricRepository.save(ProductMetric.of(productMetricEvent.productId(), productMetricEvent.unitPrice())));
                metricStrategy.process(productMetricEvent, productMetric);
                events.addAll(productMetric.events());

                break;
            }
        }
        events.forEach(applicationEventPublisher::publishEvent);
    }
}
