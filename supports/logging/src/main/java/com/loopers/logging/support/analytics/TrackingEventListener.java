package com.loopers.logging.support.analytics;

import static com.loopers.logging.config.LoggingConstant.LOGGING;
import static com.loopers.logging.support.NotificationChannel.CONSOLE;

import com.loopers.logging.support.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TrackingEventListener {

    private final NotificationSender sender;

    @Async(LOGGING)
    @EventListener(TrackingEvent.class)
    public void handle(TrackingEvent event) {
        sender.send(CONSOLE, event.message());
    }

}
