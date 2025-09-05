package com.loopers.interfaces.consumer;

import com.loopers.domain.EventLog;
import com.loopers.kafka.config.KafkaConfig;
import com.loopers.service.EventLogService;
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
public class AuditConsumer {

    private final AuditConverter auditConverter;
    private final EventLogService eventLogService;

    @KafkaListener(
        topics = {"order-events", "payment-events", "product-events", "coupon-events", "heart-events", "catalog-events"},
        groupId = "audit-group",
        containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void listener(List<ConsumerRecord<String, byte[]>> messages, Acknowledgment ack) {
        List<EventLog> logs = messages.stream().map(auditConverter::convert).toList();
        eventLogService.saveAll(logs);
        ack.acknowledge();
    }
}
