package com.loopers.event.outbox.service;

import com.loopers.event.outbox.domain.Outbox;
import com.loopers.event.outbox.support.EventEnvelope;

public interface Producer {

    boolean send(Outbox outbox);
    void send(String topic, EventEnvelope<?> eventEnvelope);
}
