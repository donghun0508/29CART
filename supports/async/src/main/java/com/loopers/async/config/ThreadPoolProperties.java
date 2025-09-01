package com.loopers.async.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "executor.default")
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
