package com.loopers.logging.support.analytics;

import java.time.Instant;
import lombok.Builder;

@Builder
public class TrackingEvent {

    private final String methodName;
    private final String className;
    private final String threadName;
    private final Instant timestamp;
    private final String phase;
    private final String arguments;

    public String message() {
        return String.format(
            "[method: %-25s], [params: %s]",
            className + "::" + methodName + "()",
            arguments
        );
    }
}
