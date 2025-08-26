package com.loopers.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import java.lang.reflect.Field;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Resilience4jConfigLogger {

    @Value("${resilience4j.log.level}")
    private String defaultLogLevel;

    public void logCircuitBreakerConfig(String name, CircuitBreaker circuitBreaker) {
        CircuitBreakerConfig config = circuitBreaker.getCircuitBreakerConfig();

        logMessage("=== CircuitBreaker [{}] Configuration ===", name);
        logMessage("  Failure Rate Threshold: {}%", config.getFailureRateThreshold());
        logMessage("  Slow Call Rate Threshold: {}%", config.getSlowCallRateThreshold());
        logMessage("  Slow Call Duration Threshold: {}ms", config.getSlowCallDurationThreshold().toMillis());
        logMessage("  Wait Duration In Open State: {}ms", config.getMaxWaitDurationInHalfOpenState().toMillis());
        logMessage("  Minimum Number Of Calls: {}", config.getMinimumNumberOfCalls());
        logMessage("  Sliding Window Size: {}", config.getSlidingWindowSize());
        logMessage("  Sliding Window Type: {}", config.getSlidingWindowType());
        logMessage("  Permitted Calls In Half Open: {}", config.getPermittedNumberOfCallsInHalfOpenState());
        logMessage("  Auto Transition Enabled: {}", config.isAutomaticTransitionFromOpenToHalfOpenEnabled());
        logMessage("  Record Result Predicate: {}", config.getRecordResultPredicate() != null ? "Custom" : "None");
    }

    public void logRetryConfig(String name, Retry retry) {
        RetryConfig config = retry.getRetryConfig();

        logMessage("=== Retry [{}] Configuration ===", name);
        logMessage("  Max Attempts: {}", config.getMaxAttempts());
        logMessage("  Fail After Max Attempts: {}", config.isFailAfterMaxAttempts());
        logMessage("  Interval Function Type: {}", getIntervalFunctionType(config));
        logMessage("  Retry On Result Predicate: {}", config.getResultPredicate() != null ? "Custom" : "None");
        logMessage("  Retry On Exception Predicate: {}, {}", config.getExceptionPredicate() != null ? "Custom" : "Default",
            getThrowablePredicateInfo(config.getExceptionPredicate()));
        logMessage("  Retry Exceptions: {}", getThrowablePredicateInfo(config.getExceptionPredicate()));
        logMessage("  Writable Stack Trace: {}", config.isWritableStackTraceEnabled());
    }

    public void logTimeLimiterConfig(String name, TimeLimiter timeLimiter) {
        TimeLimiterConfig config = timeLimiter.getTimeLimiterConfig();

        logMessage("=== TimeLimiter [{}] Configuration ===", name);
        logMessage("  Timeout Duration: {}ms", config.getTimeoutDuration().toMillis());
        logMessage("  Cancel Running Future: {}", config.shouldCancelRunningFuture());
    }

    private void logMessage(String message, Object... args) {
        switch (defaultLogLevel.toUpperCase()) {
            case "DEBUG":
                log.debug(message, args);
                break;
            case "INFO":
                log.info(message, args);
                break;
            case "WARN":
                log.warn(message, args);
                break;
            case "ERROR":
                log.error(message, args);
                break;
            case "TRACE":
                log.trace(message, args);
                break;
            default:
                log.info(message, args);
        }
    }

    private String getIntervalFunctionType(RetryConfig config) {
        var intervalBiFunction = config.getIntervalBiFunction();
        String className = intervalBiFunction.getClass().getSimpleName();

        StringBuilder details = new StringBuilder();
        details.append("Interval -> ");

        try {
            long firstAttempt = intervalBiFunction.apply(1, null);
            long secondAttempt = intervalBiFunction.apply(2, null);
            long thirdAttempt = intervalBiFunction.apply(3, null);

            Long interval = null;
            Double multiplier = null;

            try {
                Field arg1Field = intervalBiFunction.getClass().getDeclaredField("arg$1");
                arg1Field.setAccessible(true);
                Object arg1 = arg1Field.get(intervalBiFunction);

                Field[] arg1Fields = arg1.getClass().getDeclaredFields();
                for (Field field : arg1Fields) {
                    field.setAccessible(true);
                    Object value = field.get(arg1);
                    if (value instanceof Long || value instanceof Integer) {
                        interval = ((Number) value).longValue();
                        break;
                    }
                }

                Field arg2Field = arg1.getClass().getDeclaredField("arg$2");
                arg2Field.setAccessible(true);
                Object arg2 = arg2Field.get(arg1);

                Field[] arg2Fields = arg2.getClass().getDeclaredFields();
                for (Field field : arg2Fields) {
                    field.setAccessible(true);
                    Object value = field.get(arg2);
                    if (value instanceof Double) {
                        multiplier = (Double) value;
                        break;
                    }
                }
            } catch (Exception reflectionEx) {
                // 리플렉션 실패시 무시
            }

            if (interval != null && multiplier != null) {
                details.append(" (interval=").append(interval).append("ms, multiplier=").append(multiplier)
                    .append(", 1st=").append(firstAttempt).append("ms, ")
                    .append("2nd=").append(secondAttempt).append("ms, ")
                    .append("3rd=").append(thirdAttempt).append("ms)");
            } else {
                details.append(" (1st=").append(firstAttempt).append("ms, ")
                    .append("2nd=").append(secondAttempt).append("ms, ")
                    .append("3rd=").append(thirdAttempt).append("ms)");
            }

        } catch (Exception e) {
            details.append(" (분석 불가: ").append(e.getMessage()).append(")");
        }

        details.append(" [").append(intervalBiFunction.getClass().getName()).append("]");

        return details.toString();
    }

    private String getExceptionNames(Class<? extends Throwable>[] exceptions) {
        if (exceptions == null || exceptions.length == 0) {
            return "None";
        }

        return java.util.Arrays.stream(exceptions)
            .map(Class::getSimpleName)
            .collect(Collectors.joining(", "));
    }

    private String getPredicateInfo(Predicate<Object> predicate) {
        if (predicate == null) {
            return "None";
        }

        String className = predicate.getClass().getSimpleName();
        if (className.contains("Lambda") || className.contains("$")) {
            return "Custom Lambda";
        } else {
            return "Custom (" + className + ")";
        }
    }

    private String getThrowablePredicateInfo(Predicate<Throwable> predicate) {
        if (predicate == null) {
            return "None";
        }

        String className = predicate.getClass().getSimpleName();
        if (className.contains("Lambda") || className.contains("$")) {
            return "Custom Lambda";
        } else {
            return "Custom (" + className + ")";
        }
    }
}
