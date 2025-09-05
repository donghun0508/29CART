package com.loopers.event.outbox.domain;

import lombok.Getter;
import lombok.ToString;

public class OutboxEvent {

    @Getter
    @ToString
    public static class OutboxCreatedEvent {

        private Outbox outbox;

        public static OutboxCreatedEvent from(Outbox outbox) {
            OutboxCreatedEvent outboxCreatedEvent = new OutboxCreatedEvent();
            outboxCreatedEvent.outbox = outbox;
            return outboxCreatedEvent;
        }
    }
}
