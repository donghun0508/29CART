package com.loopers.service.strategy.metric;

import com.loopers.domain.ProductMetric;
import com.loopers.service.ProductMetricAnalysis;
import com.loopers.service.PayloadMapper;
import com.loopers.service.strategy.metric.ProductMetricDecreaseLikeStrategy.ProductHeartDecreasedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class ProductMetricDecreaseLikeStrategy implements ProductMetricStrategy<ProductHeartDecreasedEvent> {

    private final PayloadMapper payloadMapper;

    @Override
    public boolean supports(ProductMetricAnalysis productMetricAnalysis) {
        return "ProductHeartDecreasedEvent".equals(productMetricAnalysis.getType());
    }

    @Override
    public ProductHeartDecreasedEvent getProductId(String payload) {
        return payloadMapper.parsePayload(payload, ProductHeartDecreasedEvent.class);
    }

    @Override
    public void process(ProductHeartDecreasedEvent event, ProductMetric productMetric) {
        productMetric.decrementLikeCount();
    }

    public record ProductHeartDecreasedEvent(Long productId, Long heartCount) implements ProductMetricEvent {

        @Override
        public List<Long> productIds() {
            return List.of(productId);
        }
    }
}
