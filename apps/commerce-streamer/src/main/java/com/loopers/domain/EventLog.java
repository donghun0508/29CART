package com.loopers.domain;


import com.loopers.common.domain.CreatedOnlyEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(
    name = "event_log",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_EVENT_LOG_MESSAGE_KEY_EVENT_ID",
            columnNames = {"message_key", "event_id"}
        )
    }
)
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class EventLog extends CreatedOnlyEntity {

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "partition_no")
    private Integer partition;

    @Column(name = "record_offset")
    private Long offset;

    @Column(name = "record_timestamp")
    private Long timestamp;

    @Column(name = "message_key")
    private String key;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "json")
    private String payload;

    @Column(name = "payload_size")
    private Integer payloadSize;

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "causation_id")
    private String causationId;

    @Column(name = "aggregate_type")
    private String aggregateType;

    @Column(name = "schema_version")
    private Integer schemaVersion;

    @Column(name = "occurred_at")
    private java.time.Instant occurredAt;

    @Column(name = "producer")
    private String producer;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "headers", columnDefinition = "json")
    private String headersJson;
}
