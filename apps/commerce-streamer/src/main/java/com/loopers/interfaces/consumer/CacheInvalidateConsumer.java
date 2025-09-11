package com.loopers.interfaces.consumer;

import com.loopers.domain.CacheInvalidateType;
import com.loopers.domain.EventLog;
import com.loopers.kafka.config.KafkaConfig;
import com.loopers.service.CacheInvalidate;
import com.loopers.service.CacheInvalidateService;
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
public class CacheInvalidateConsumer {

    private final CacheInvalidateConverter cacheInvalidateConverter;
    private final CacheInvalidateService cacheInvalidateService;

    @KafkaListener(
        topics = {"heart-events", "catalog-events"},
        groupId = "cache-invalidate-group",
        containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void listener(List<ConsumerRecord<String, byte[]>> messages, Acknowledgment ack) {
        List<CacheInvalidate> cacheInvalidateTypes = messages.stream().map(cacheInvalidateConverter::convert).toList();
        cacheInvalidateTypes.forEach(cacheInvalidateService::invalidate);
        ack.acknowledge();
    }
}
