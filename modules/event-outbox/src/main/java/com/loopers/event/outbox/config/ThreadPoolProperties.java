package com.loopers.event.outbox.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "executor.event")
@Data
public class ThreadPoolProperties {

    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private String threadNamePrefix;
    private int keepAliveSeconds;
    private boolean allowCoreThreadTimeout;
    private boolean waitForTasksToCompleteOnShutdown;
    private int awaitTerminationSeconds;
}
