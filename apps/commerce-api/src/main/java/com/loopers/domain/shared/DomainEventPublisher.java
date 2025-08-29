package com.loopers.domain.shared;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(List<DomainEvent> events) {
        events.forEach(applicationEventPublisher::publishEvent);
    }
}
