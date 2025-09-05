package com.loopers.event.outbox;

import com.loopers.common.domain.AggregateRoot;
import com.loopers.common.domain.DomainEvent;
import com.loopers.event.outbox.domain.Outbox;
import com.loopers.event.outbox.domain.OutboxEvent.OutboxCreatedEvent;
import com.loopers.event.outbox.service.OutboxFacade;
import com.loopers.event.outbox.support.EventEnvelope;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventStore {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final EventEnvelopeFactory envelopeFactory;
    private final OutboxFacade outboxFacade;

    public void save(AggregateRoot aggregate) {
        List<EventEnvelope<? extends DomainEvent>> envelopes = envelopeFactory.create(aggregate);
        for (EventEnvelope<? extends DomainEvent> env : envelopes) {
            Outbox outbox = Outbox.from(env);
            outboxFacade.save(outbox);
            applicationEventPublisher.publishEvent(OutboxCreatedEvent.from(outbox));
        }
        envelopes.forEach(applicationEventPublisher::publishEvent);
    }
}
