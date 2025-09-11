package com.loopers.service.strategy.metric;

import com.loopers.domain.ProductMetric;
import com.loopers.service.ProductMetricAnalysis;
import com.loopers.service.PayloadMapper;
import com.loopers.service.strategy.metric.ProductMetricIncreaseLikeStrategy.ProductHeartIncreasedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class ProductMetricIncreaseLikeStrategy implements ProductMetricStrategy<ProductHeartIncreasedEvent> {

    private final PayloadMapper payloadMapper;

    @Override
    public boolean supports(ProductMetricAnalysis productMetricAnalysis) {
        return "ProductHeartIncreasedEvent".equals(productMetricAnalysis.getType());
    }

    @Override
    public ProductHeartIncreasedEvent getProductId(String payload) {
        return payloadMapper.parsePayload(payload,
            ProductHeartIncreasedEvent.class);
    }

    @Override
    public void process(ProductHeartIncreasedEvent event, ProductMetric productMetric) {
        productMetric.incrementLikeCount();
    }

    public record ProductHeartIncreasedEvent(Long productId, Long heartCount) implements  ProductMetricEvent {
        @Override
        public List<Long> productIds() {
            return List.of(productId);
        }
    }
}
