package com.loopers.kafka;


import com.loopers.event.outbox.domain.Outbox;
import com.loopers.event.outbox.service.Producer;
import com.loopers.event.outbox.support.EventEnvelope;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class KafkaProducer implements Producer {

    private final KafkaTemplate<String, Object> messageRelayKafkaTemplate;
    private final KafkaTopicRouter kafkaTopicRouter;

    public boolean send(Outbox outbox) {
        String topic = kafkaTopicRouter.getTopic(outbox.getEventType());

        String key = outbox.getAggregateId();

        ProducerRecord<String, Object> record = new ProducerRecord<>(
            topic,
            key,
            outbox.getPayloadJson()
        );

        if (outbox.getEventId() != null) {
            record.headers().add("eventId", outbox.getEventId().getBytes(StandardCharsets.UTF_8));
        }
        if (outbox.getEventType() != null) {
            record.headers().add("eventType", outbox.getEventType().getBytes(StandardCharsets.UTF_8));
        }
        if (outbox.getCorrelationId() != null) {
            record.headers().add("correlationId", outbox.getCorrelationId().getBytes(StandardCharsets.UTF_8));
        }
        if (outbox.getCausationId() != null) {
            record.headers().add("causationId", outbox.getCausationId().getBytes(StandardCharsets.UTF_8));
        }
        if (outbox.getAggregateType() != null) {
            record.headers().add("aggregateType", outbox.getAggregateType().getBytes(StandardCharsets.UTF_8));
        }
        if (outbox.getAggregateId() != null) {
            record.headers().add("aggregateId", outbox.getAggregateId().getBytes(StandardCharsets.UTF_8));
        }
        if (outbox.getSchemaVersion() != null) {
            record.headers().add("schemaVersion", String.valueOf(outbox.getSchemaVersion()).getBytes(StandardCharsets.UTF_8));
        }
        if (outbox.getOccurredAt() != null) {
            record.headers().add("occurredAt", outbox.getOccurredAt().toString().getBytes(StandardCharsets.UTF_8));
        }
        if (outbox.getProducer() != null) {
            record.headers().add("producer", outbox.getProducer().getBytes(StandardCharsets.UTF_8));
        }

        try {
            messageRelayKafkaTemplate.send(record).get(1, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("[EventProducer.send] outbox={}", outbox, e);
            return false;
        }
    }

    @Override
    public void send(String topic, EventEnvelope<?> envelope) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(
            topic,
            envelope.metadata().aggregateId(),
            envelope.payloadToJson()
        );

        if (envelope.metadata().eventId() != null) {
            record.headers().add("eventId", envelope.metadata().eventId().getBytes(StandardCharsets.UTF_8));
        }
        if (envelope.metadata().eventType() != null) {
            record.headers().add("eventType", envelope.metadata().eventType().getBytes(StandardCharsets.UTF_8));
        }
        if (envelope.metadata().correlationId() != null) {
            record.headers().add("correlationId", envelope.metadata().correlationId().getBytes(StandardCharsets.UTF_8));
        }
        if (envelope.metadata().causationId() != null) {
            record.headers().add("causationId", envelope.metadata().causationId().getBytes(StandardCharsets.UTF_8));
        }
        if (envelope.metadata().aggregateType() != null) {
            record.headers().add("aggregateType", envelope.metadata().aggregateType().getBytes(StandardCharsets.UTF_8));
        }
        if (envelope.metadata().aggregateId() != null) {
            record.headers().add("aggregateId", envelope.metadata().aggregateId().getBytes(StandardCharsets.UTF_8));
        }
        record.headers()
            .add("schemaVersion", String.valueOf(envelope.metadata().schemaVersion()).getBytes(StandardCharsets.UTF_8));
        if (envelope.metadata().occurredAt() != null) {
            record.headers().add("occurredAt", envelope.metadata().occurredAt().toString().getBytes(StandardCharsets.UTF_8));
        }
        if (envelope.metadata().producer()!= null) {
            record.headers().add("producer", envelope.metadata().producer().getBytes(StandardCharsets.UTF_8));
        }

        try {
            messageRelayKafkaTemplate.send(record).get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("[EventProducer.send] ", e);
        }
    }
}
