package com.loopers.event.outbox.support;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.loopers.common.domain.DomainEvent;
import com.loopers.event.outbox.DataSerializer;
import java.time.Instant;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public record EventEnvelope<T extends DomainEvent>(
    EventMetadata metadata,
    T payload
) implements ResolvableTypeProvider {

    @JsonIgnore
    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
            getClass(), ResolvableType.forInstance(payload)
        );
    }

    public record EventMetadata(
        String eventId,        // ULID/UUID
        String eventType,      // 예: "OrderCreatedEvent"
        int schemaVersion,  // 예: 1
        String correlationId,  // 여정/사가 ID (최초면 새로 생성)
        String causationId,    // 직전 이벤트/커맨드 ID (최초면 null)
        String aggregateType,  // 예: "Order"
        String aggregateId,    // 예: orderNumber 또는 외부 비즈니스 ID
        Instant occurredAt,    // UTC now
        String producer        // 예: "order-service"
    ) {
    }

    public String toJson() {
        return DataSerializer.serialize(this);
    }

    public String payloadToJson() {
        return DataSerializer.serialize(payload);
    }
}
