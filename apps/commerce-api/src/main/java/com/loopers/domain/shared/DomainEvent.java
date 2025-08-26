package com.loopers.domain.shared;

import java.time.ZonedDateTime;

public interface DomainEvent {

    default ZonedDateTime occurredOn() {
        return ZonedDateTime.now();
    }
}
