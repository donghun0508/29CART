package com.loopers.service.strategy.metric;

import com.loopers.domain.ProductMetric;
import com.loopers.service.PayloadMapper;
import com.loopers.service.ProductMetricAnalysis;
import com.loopers.service.strategy.metric.ProductMetricStockRestoredStrategy.ProductStockRestoredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductMetricStockRestoredStrategy implements ProductMetricStrategy<ProductStockRestoredEvent> {

    private final PayloadMapper payloadMapper;

    @Override
    public boolean supports(ProductMetricAnalysis productMetricAnalysis) {
        return "ProductStockRestoredEvent".equals(productMetricAnalysis.getType());
    }

    @Override
    public ProductStockRestoredEvent getProductId(String payload) {
        return payloadMapper.parsePayload(payload, ProductStockRestoredEvent.class);
    }

    @Override
    public void process(ProductStockRestoredEvent event, ProductMetric productMetric) {
        productMetric.decreaseStock(event.restoredStock);
    }

    public record ProductStockRestoredEvent(Long productId, Long restoredStock, Long unitPrice) implements ProductMetricEvent {

    }

}
