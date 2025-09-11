package com.loopers.event.outbox.service;

import com.loopers.event.outbox.domain.Outbox;
import com.loopers.event.outbox.domain.OutboxStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutboxFacade {

    private final Producer producer;
    private final OutboxService outboxService;

    public void save(Outbox outbox) {
        outboxService.save(outbox);
    }

    public void dispatchPendingEvents() {
        List<String> retryableIds = outboxService
            .findAllByStatusIn(List.of(OutboxStatus.PENDING, OutboxStatus.FAILED))
            .stream()
            .map(Outbox::getEventId)
            .toList();

        retryableIds.forEach(this::dispatchEvent);
    }

    public void dispatchEvent(String eventId) {
        Outbox outbox = outboxService.findByEventId(eventId);
        boolean result = producer.send(outbox);
        outboxService.recordResult(eventId, result);
    }
}
