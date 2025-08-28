package com.loopers.logging.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logging.executor")
@Data
public class LoggingThreadPoolProperties {

    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private String threadNamePrefix;
    private int keepAliveSeconds;
    private boolean allowCoreThreadTimeout;
    private boolean waitForTasksToCompleteOnShutdown;
    private int awaitTerminationSeconds;
}
