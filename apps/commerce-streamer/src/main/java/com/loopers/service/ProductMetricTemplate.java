package com.loopers.service;

import com.loopers.domain.ProductMetric;
import com.loopers.repository.ProductMetricRepository;
import com.loopers.service.strategy.metric.ProductMetricStrategy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class ProductMetricTemplate {

    private final ProductMetricRepository productMetricRepository;
    private final List<ProductMetricStrategy> strategies;

    @Transactional
    public void execute(ProductMetricAnalysis productMetricAnalysis) {
        for (ProductMetricStrategy<?> strategy : strategies) {
            if(strategy.supports(productMetricAnalysis)) {
                ProductMetricStrategy<ProductMetricStrategy.ProductMetricEvent> s = (ProductMetricStrategy<ProductMetricStrategy.ProductMetricEvent>) strategy;
                ProductMetricStrategy.ProductMetricEvent productMetricEvent = s.getProductId(productMetricAnalysis.getPayload());
                for (Long productId : productMetricEvent.productIds()) {
                    ProductMetric productMetric = productMetricRepository.findByProductId(productId).orElseGet(() -> productMetricRepository.save(ProductMetric.of(productId)));
                    s.process(productMetricEvent, productMetric);
                }
                break;
            }
        }
    }

}
