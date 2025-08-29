package com.loopers.logging.support.alert;

@FunctionalInterface
public interface NotificationEvent {

    String message();

    default boolean shouldNotify() {
        return true;
    }
}
