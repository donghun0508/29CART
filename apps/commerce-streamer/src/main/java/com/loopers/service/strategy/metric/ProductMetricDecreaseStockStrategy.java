package com.loopers.service.strategy.metric;

import com.loopers.domain.ProductMetric;
import com.loopers.service.ProductMetricAnalysis;
import com.loopers.service.PayloadMapper;
import com.loopers.service.strategy.metric.ProductMetricDecreaseStockStrategy.ProductStockRestoredEvent;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductMetricDecreaseStockStrategy implements ProductMetricStrategy<ProductStockRestoredEvent> {

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
        Long restoreCount = event.restoredStockMap.get(productMetric.getProductId());
        productMetric.decreaseStock(restoreCount);
    }

    public record ProductStockRestoredEvent(Map<Long, Long> restoredStockMap) implements ProductMetricEvent {

        @Override
        public List<Long> productIds() {
            return restoredStockMap.keySet().stream().toList();
        }
    }
}
