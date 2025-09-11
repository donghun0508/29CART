package com.loopers.event.outbox.domain;


import com.loopers.common.domain.BaseEntity;
import com.loopers.common.domain.DomainEvent;
import com.loopers.event.outbox.support.EventEnvelope;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "outbox_event",
    indexes = {
        @Index(name = "idx_sched", columnList = "status, next_attempt_at, created_at"),
        @Index(name = "uk_event_id", columnList = "event_id", unique = true),
        @Index(name = "ix_agg_timeline", columnList = "aggregate_type, aggregate_id, occurred_at"),
        @Index(name = "ix_corr", columnList = "correlation_id"),
        @Index(name = "ix_event_type", columnList = "event_type")
    }
)
public class Outbox extends BaseEntity {

    @Column(name = "event_id", length = 64, nullable = false, unique = true)
    private String eventId;

    @Column(name = "event_type", length = 100, nullable = false)
    private String eventType;

    @Column(name = "schema_version", nullable = false)
    private Integer schemaVersion;

    @Column(name = "correlation_id", length = 128)
    private String correlationId;

    @Column(name = "causation_id", length = 64)
    private String causationId;

    @Column(name = "aggregate_type", length = 50, nullable = false)
    private String aggregateType;

    @Column(name = "aggregate_id", length = 128, nullable = false)
    private String aggregateId;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "producer", length = 100, nullable = false)
    private String producer;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "json", nullable = false)
    private String payloadJson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "envelope", columnDefinition = "json")
    private String envelopeJson;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OutboxStatus status;

    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount = 0;

    @Column(name = "next_attempt_at")
    private ZonedDateTime nextAttemptAt;

    public static Outbox from(EventEnvelope<? extends DomainEvent> env) {
        var m = env.metadata();

        Outbox ob = new Outbox();
        ob.eventId = m.eventId();
        ob.eventType = m.eventType();
        ob.schemaVersion = m.schemaVersion();
        ob.correlationId = m.correlationId();
        ob.causationId = m.causationId();
        ob.aggregateType = m.aggregateType();
        ob.aggregateId = m.aggregateId();
        ob.occurredAt = m.occurredAt();
        ob.producer = m.producer();

        ob.payloadJson = env.payloadToJson();
        ob.envelopeJson = env.toJson();

        ob.status = OutboxStatus.PENDING;
        ob.attemptCount = 0;
        ob.nextAttemptAt = null;

        return ob;
    }

    public void markAsSent() {
        this.status = OutboxStatus.SENT;
    }

    public void markAsFailed() {
        this.status = OutboxStatus.FAILED;
        this.attemptCount++;
        this.nextAttemptAt = ZonedDateTime.now().plusSeconds(60);
    }
}
