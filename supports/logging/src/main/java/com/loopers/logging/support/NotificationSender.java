package com.loopers.logging.support;

public interface NotificationSender {

    void send(NotificationChannel gatewayType, String message);
}
