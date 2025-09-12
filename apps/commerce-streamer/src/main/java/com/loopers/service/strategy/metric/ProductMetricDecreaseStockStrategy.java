package com.loopers.service.strategy.metric;

import com.loopers.domain.ProductMetric;
import com.loopers.service.PayloadMapper;
import com.loopers.service.ProductMetricAnalysis;
import com.loopers.service.strategy.metric.ProductMetricDecreaseStockStrategy.ProductStockDecreasedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductMetricDecreaseStockStrategy implements ProductMetricStrategy<ProductStockDecreasedEvent> {

    private final PayloadMapper payloadMapper;

    @Override
    public boolean supports(ProductMetricAnalysis productMetricAnalysis) {
        return "ProductStockDecreasedEvent".equals(productMetricAnalysis.getType());
    }

    @Override
    public ProductStockDecreasedEvent getProductId(String payload) {
        return payloadMapper.parsePayload(payload, ProductStockDecreasedEvent.class);
    }

    @Override
    public void process(ProductStockDecreasedEvent event, ProductMetric productMetric) {
        productMetric.increaseStock(event.decreasedStock);
    }

    public record ProductStockDecreasedEvent(Long productId, Long decreasedStock, Long unitPrice) implements ProductMetricEvent {

    }
}
