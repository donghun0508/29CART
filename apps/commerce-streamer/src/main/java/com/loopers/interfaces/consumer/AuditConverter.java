package com.loopers.interfaces.consumer;

import static com.loopers.utils.ConverterUtil.header;
import static com.loopers.utils.ConverterUtil.headersAsJson;
import static com.loopers.utils.ConverterUtil.parseInt;

import com.loopers.domain.EventLog;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuditConverter implements Converter<ConsumerRecord<String, byte[]>, EventLog> {

    @Override
    public EventLog convert(ConsumerRecord<String, byte[]> source) {
        String topic = source.topic();
        int partition = source.partition();
        long offset = source.offset();
        long ts = source.timestamp();

        String key = source.key();
        String value = source.value() != null
            ? new String(source.value(), StandardCharsets.UTF_8)
            : null;

        Headers h = source.headers();
        String eventId = header(h, "eventId");
        String eventType = header(h, "eventType");
        String correlationId = header(h, "correlationId");
        String causationId = header(h, "causationId");
        String aggregateType = header(h, "aggregateType");
        Integer schemaVer = parseInt(header(h, "schemaVersion"));
        String occurredAtStr = header(h, "occurredAt");
        String producer = header(h, "producer");

        Instant occurredAt = occurredAtStr != null ? Instant.parse(occurredAtStr) : null;

        return EventLog.builder()
            .topic(topic)
            .partition(partition)
            .offset(offset)
            .timestamp(ts)
            .key(key)
            .payload(value)
            .payloadSize(value != null ? value.length() : 0)
            .eventId(eventId)
            .eventType(eventType)
            .correlationId(correlationId)
            .causationId(causationId)
            .aggregateType(aggregateType)
            .schemaVersion(schemaVer)
            .occurredAt(occurredAt)
            .producer(producer)
            .headersJson(headersAsJson(h))
            .build();
    }


}
