package com.loopers.service.strategy.metric;

import com.loopers.domain.ProductMetric;
import com.loopers.service.ProductMetricAnalysis;
import com.loopers.service.PayloadMapper;
import com.loopers.service.strategy.metric.ProductMetricDetailViewStrategy.ProductDetailViewedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class ProductMetricDetailViewStrategy implements ProductMetricStrategy<ProductDetailViewedEvent> {

    private final PayloadMapper payloadMapper;

    @Override
    public boolean supports(ProductMetricAnalysis productMetricAnalysis) {
        return "ProductDetailViewedEvent".equals(productMetricAnalysis.getType());
    }

    @Override
    public ProductDetailViewedEvent getProductId(String payload) {
        return payloadMapper.parsePayload(payload, ProductDetailViewedEvent.class);
    }

    @Override
    public void process(ProductDetailViewedEvent event, ProductMetric productMetric) {
        productMetric.incrementDetailViewCount();
    }

    public record ProductDetailViewedEvent(String type, Long productId) implements ProductMetricEvent {
        @Override
        public List<Long> productIds() {
            return List.of(productId);
        }
    }
}
