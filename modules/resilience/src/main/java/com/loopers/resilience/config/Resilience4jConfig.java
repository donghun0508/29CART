package com.loopers.resilience.config;

import java.util.List;
import lombok.Data;

@Data
public class Resilience4jConfig {

    private CustomCircuitBreakerConfig circuitbreaker = new CustomCircuitBreakerConfig();
    private CustomRetryConfig retry = new CustomRetryConfig();
    private CustomTimeLimiterConfig timelimiter = new CustomTimeLimiterConfig();

    @Data
    public static class CustomCircuitBreakerConfig {

        private Float failureRateThreshold;
        private Float slowCallRateThreshold;
        private Long slowCallDurationThreshold;
        private Long waitDurationInOpenState;
        private Integer minimumNumberOfCalls;
        private Integer slidingWindowSize;
        private Integer permittedNumberOfCallsInHalfOpenState;
        private Boolean automaticTransitionFromOpenToHalfOpenEnabled;
        private List<String> recordExceptions;
    }

    @Data
    public static class CustomRetryConfig {

        private Integer maxAttempts;
        private Long waitDuration;
        private Double exponentialBackoffMultiplier;
        private Boolean enableExponentialBackoff;
        private Boolean manual;
        private List<String> retryExceptions;
        private List<String> ignoreExceptions;
    }

    @Data
    public static class CustomTimeLimiterConfig {

        private Long timeoutDuration;
        private Boolean cancelRunningFuture;
    }
}
