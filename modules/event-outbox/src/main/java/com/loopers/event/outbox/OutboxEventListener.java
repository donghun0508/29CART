package com.loopers.event.outbox;

import static com.loopers.event.outbox.config.EventAsyncConstant.EVENT;

import com.loopers.event.outbox.domain.OutboxEvent.OutboxCreatedEvent;
import com.loopers.event.outbox.service.OutboxFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
class OutboxEventListener {

    private final OutboxFacade outboxFacade;

    @Async(EVENT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(OutboxCreatedEvent outboxCreatedEvent) {
        log.debug("[MessageRelay.afterCommit] outboxEvent={}", outboxCreatedEvent);
        outboxFacade.dispatchEvent(outboxCreatedEvent.getOutbox().getEventId());
    }
}
