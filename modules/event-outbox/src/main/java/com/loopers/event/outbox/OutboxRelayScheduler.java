package com.loopers.event.outbox;

import static com.loopers.event.outbox.config.EventAsyncConstant.RELAY;

import com.loopers.event.outbox.service.OutboxFacade;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class OutboxRelayScheduler {

    private final OutboxFacade outboxFacade;

    @Scheduled(
        fixedDelay = 10,
        initialDelay = 5,
        timeUnit = TimeUnit.SECONDS,
        scheduler = RELAY
    )
    public void retryPendingEvents() {
        outboxFacade.dispatchPendingEvents();
    }
}
