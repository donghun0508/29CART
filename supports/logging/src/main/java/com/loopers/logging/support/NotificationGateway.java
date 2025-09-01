package com.loopers.logging.support;

public interface NotificationGateway {

    NotificationChannel channel();

    void send(String message);
}
