package com.loopers.service.strategy.metric;

import com.loopers.domain.ProductMetric;
import com.loopers.service.ProductMetricAnalysis;
import com.loopers.service.PayloadMapper;
import com.loopers.service.strategy.metric.ProductMetricIncreaseStockStrategy.OrderCreatedEvent;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductMetricIncreaseStockStrategy implements ProductMetricStrategy<OrderCreatedEvent> {

    private final PayloadMapper payloadMapper;

    @Override
    public boolean supports(ProductMetricAnalysis productMetricAnalysis) {
        return "OrderCreatedEvent".equals(productMetricAnalysis.getType());
    }

    @Override
    public OrderCreatedEvent getProductId(String payload) {
        return payloadMapper.parsePayload(payload,
            OrderCreatedEvent.class);
    }

    @Override
    public void process(OrderCreatedEvent event, ProductMetric productMetric) {
        Long stock = event.orderLines.get(productMetric.getProductId());
        productMetric.increaseStock(stock);
    }

    public record OrderCreatedEvent(
        Long buyerId,
        String status,
        Map<Long, Long> orderLines,
        Long totalAmount,
        Long paidAmount,
        Long issuedCouponId,
        String idempotencyKey,
        String orderNumber
    ) implements ProductMetricEvent {

        @Override
        public List<Long> productIds() {
            return orderLines.keySet().stream().toList();
        }
    }
}
