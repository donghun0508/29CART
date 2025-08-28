package com.loopers.logging.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class NotificationSenderImpl implements  NotificationSender{
    private final NotificationPort notificationPort;

    public void send(String message) {
        notificationPort.send(message);
    }
}
