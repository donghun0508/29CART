package com.loopers.interfaces.consumer;

import com.loopers.kafka.config.KafkaConfig;
import com.loopers.service.IdempotencyService;
import com.loopers.service.ProductMetricAnalysis;
import com.loopers.service.ProductMetricService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MetricConsumer {

    private final MetricConverter metricConverter;
    private final ProductMetricService productMetricService;
    private final IdempotencyService idempotencyService;

    @KafkaListener(
        topics = {"catalog-events", "order-events"},
        groupId = "metric-group",
        containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void listener(List<ConsumerRecord<String, byte[]>> messages, Acknowledgment ack) {
        List<ProductMetricAnalysis> productMetricAnalyses = messages.stream().map(metricConverter::convert).toList();

        for (ProductMetricAnalysis analysis : productMetricAnalyses) {
            if (idempotencyService.alreadyProcessed(analysis.getPayload())) {
                log.debug("[MetricConsumer.listener] Skipping already processed payload: {}", analysis.getPayload());
                continue;
            }
            productMetricService.save(analysis);
        }
        ack.acknowledge();
    }
}
