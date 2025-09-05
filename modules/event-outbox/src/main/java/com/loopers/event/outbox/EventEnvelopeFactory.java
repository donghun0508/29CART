package com.loopers.event.outbox;

import com.loopers.common.domain.AggregateRoot;
import com.loopers.common.domain.DomainEvent;
import com.loopers.event.outbox.support.EventEnvelope;
import com.loopers.event.outbox.support.EventEnvelope.EventMetadata;
import com.loopers.event.outbox.support.EventPropagation;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventEnvelopeFactory {

    private final EventPropagation eventPropagation;

    private final Clock clock = Clock.systemUTC();
    private final int schemaVersion = 1;

    public List<EventEnvelope<? extends DomainEvent>> create(AggregateRoot aggregate) {
        var events = aggregate.events();
        if (events == null || events.isEmpty()) {
            return List.of();
        }

        final String aggType = aggregate.aggregateType();
        final String aggIdFromAgg = aggregate.businessId();

        final String corrFromBag = nz(eventPropagation.get("correlationId"));
        final String causeFromBag = nz(eventPropagation.get("causationId"));
        final String rootFromBag = nz(eventPropagation.get("rootAggregateId"));

        final String rootFromCorr = stripSaga(corrFromBag);
        final String sagaAggId = firstNonBlank(rootFromBag, rootFromCorr, aggIdFromAgg);

        final String corrFixed = !isBlank(corrFromBag) ? corrFromBag : (!isBlank(sagaAggId) ? "SAGA-" + sagaAggId : null);

        var out = new ArrayList<EventEnvelope<? extends DomainEvent>>(events.size());
        for (DomainEvent ev : events) {
            if (isBlank(causeFromBag)) {
                out.add(startInternal(aggType, nullIfBlank(sagaAggId), ev, corrFixed));
            } else {
                out.add(followUpInternal(aggType, nullIfBlank(sagaAggId), ev, corrFixed, causeFromBag));
            }
        }
        return List.copyOf(out);
    }

    private <T extends DomainEvent> EventEnvelope<T> startInternal(String aggregateType, String aggregateId, T payload, String corr) {
        String eventType = payload.getClass().getSimpleName();
        var md = new EventEnvelope.EventMetadata(
            UUID.randomUUID().toString(),
            eventType,
            schemaVersion,
            corr,
            null,
            aggregateType,
            aggregateId,
            Instant.now(clock),
            getProducerName(eventType)
        );
        return new EventEnvelope<>(md, payload);
    }

    private <T extends DomainEvent> EventEnvelope<T> followUpInternal(String aggregateType, String rootAggregateId, T payload, String corr, String parentEventId) {
        String eventType = payload.getClass().getSimpleName();
        var md = new EventEnvelope.EventMetadata(
            UUID.randomUUID().toString(),
            eventType,
            schemaVersion,
            corr,
            parentEventId,
            aggregateType,
            rootAggregateId,
            Instant.now(clock),
            getProducerName(eventType)
        );
        return new EventEnvelope<>(md, payload);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private String nz(String s) {
        return s == null ? "" : s;
    }

    private String nullIfBlank(String s) {
        return isBlank(s) ? null : s;
    }

    private String firstNonBlank(String... vs) {
        for (String v : vs) {
            if (!isBlank(v)) {
                return v;
            }
        }
        return null;
    }

    private String stripSaga(String corr) {
        return (corr != null && corr.startsWith("SAGA-") && corr.length() > 5)
            ? corr.substring(5) : null;
    }

    private String getProducerName(String eventType) {
        if (eventType == null || eventType.isBlank()) {
            return "commerce-api";
        }
        if (eventType.contains("Payment")) {
            return "payment-service";
        }
        if(eventType.contains("Product")) {
            return "catalog-service";
        }
        String prefix = eventType.replaceAll("([A-Z][a-zA-Z0-9]+?)([A-Z].*)", "$1");
        return prefix.toLowerCase() + "-service";
    }
}
