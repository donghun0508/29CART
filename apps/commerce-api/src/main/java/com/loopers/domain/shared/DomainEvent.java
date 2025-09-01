package com.loopers.domain.shared;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class DomainEvent {

    private final String eventId;
    private final ZonedDateTime occurredOn;
    private final String name;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = ZonedDateTime.now();
        this.name = getClass().getSimpleName();
    }
}
