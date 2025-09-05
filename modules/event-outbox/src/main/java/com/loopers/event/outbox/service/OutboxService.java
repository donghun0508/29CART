package com.loopers.event.outbox.service;

import com.loopers.event.outbox.domain.Outbox;
import com.loopers.event.outbox.domain.OutboxRepository;
import com.loopers.event.outbox.domain.OutboxStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OutboxService {

    private final OutboxRepository outboxRepository;

    @Transactional
    public void save(Outbox outbox) {
        outboxRepository.save(outbox);
    }

    @Transactional
    public void recordResult(String eventId, boolean result) {
        Outbox outbox = outboxRepository.findByEventId(eventId).orElseThrow();
        if (result) {
            outbox.markAsSent();
        } else {
            outbox.markAsFailed();
        }
    }

    @Transactional(readOnly = true)
    public Outbox findByEventId(String eventId) {
        return outboxRepository.findByEventId(eventId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Outbox> findAllByStatusIn(List<OutboxStatus> outboxStatuses) {
        return outboxRepository.findAllByStatusIn(outboxStatuses);
    }
}
