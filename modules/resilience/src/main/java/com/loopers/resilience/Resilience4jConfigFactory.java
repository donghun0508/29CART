package com.loopers.resilience;

import static io.github.resilience4j.retry.RetryConfig.*;
import static java.util.Optional.ofNullable;

import com.loopers.resilience.config.Resilience4jConfig.CustomCircuitBreakerConfig;
import com.loopers.resilience.config.Resilience4jConfig.CustomRetryConfig;
import com.loopers.resilience.config.Resilience4jConfig.CustomTimeLimiterConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.IntervalBiFunction;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Resilience4jConfigFactory {

    private static final String DEFAULT_CONFIG_NAME = "default";

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;
    private final TimeLimiterRegistry timeLimiterRegistry;
    private final ExceptionClassConverter exceptionClassConverter;
    private final Resilience4jConfigLogger resilience4jConfigLogger;

    public CircuitBreaker createCircuitBreaker(String name, CircuitBreakerConfig customConfig) {
        if (customConfig == null) {
            return circuitBreakerRegistry.circuitBreaker(name);
        }
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(name, customConfig);
        resilience4jConfigLogger.logCircuitBreakerConfig(name, circuitBreaker);
        return circuitBreaker;
    }

    public CircuitBreaker createCircuitBreaker(String name, Consumer<CircuitBreakerConfig.Builder> customizer) {
        var defaultConfig = circuitBreakerRegistry.getConfiguration(DEFAULT_CONFIG_NAME)
            .orElse(circuitBreakerRegistry.getDefaultConfig());

        var builder = CircuitBreakerConfig.from(defaultConfig);
        customizer.accept(builder);
        var finalConfig = builder.build();

        return circuitBreakerRegistry.circuitBreaker(name, finalConfig);
    }

    public Retry createRetry(String name, RetryConfig customConfig) {
        if (customConfig == null) {
            return retryRegistry.retry(name);
        }
        return retryRegistry.retry(name, customConfig);
    }

    public Retry createRetry(String name, Consumer<RetryConfig.Builder> customizer) {
        var defaultConfig = retryRegistry.getConfiguration(DEFAULT_CONFIG_NAME)
            .orElse(retryRegistry.getDefaultConfig());

        var builder = RetryConfig.from(defaultConfig);
        customizer.accept(builder);
        var finalConfig = builder.build();

        return retryRegistry.retry(name, finalConfig);
    }

    public TimeLimiter createTimeLimiter(String name, TimeLimiterConfig customConfig) {
        if (customConfig == null) {
            return timeLimiterRegistry.timeLimiter(name);
        }
        return timeLimiterRegistry.timeLimiter(name, customConfig);
    }

    public TimeLimiter createTimeLimiter(String name, Consumer<TimeLimiterConfig.Builder> customizer) {
        var defaultConfig = timeLimiterRegistry.getConfiguration(DEFAULT_CONFIG_NAME)
            .orElse(timeLimiterRegistry.getDefaultConfig());

        var builder = TimeLimiterConfig.from(defaultConfig);
        customizer.accept(builder);
        var finalConfig = builder.build();

        return timeLimiterRegistry.timeLimiter(name, finalConfig);
    }

    public CircuitBreaker createCircuitBreaker(String name, CustomCircuitBreakerConfig customConfig) {
        var circuitBreaker = createCircuitBreaker(name, builder -> applyCustomCircuitBreakerConfig(builder, customConfig));
        resilience4jConfigLogger.logCircuitBreakerConfig(name, circuitBreaker);
        return circuitBreaker;
    }

    public Retry createRetry(String name, CustomRetryConfig customConfig) {
        var retry = createRetry(name, builder -> applyCustomRetryConfig(builder, customConfig, null));
        resilience4jConfigLogger.logRetryConfig(name, retry);
        return retry;
    }

    public Retry createRetry(String name, CustomRetryConfig customConfig, Predicate<Throwable> manualPredicate) {
        var retry = createRetry(name, builder -> applyCustomRetryConfig(builder, customConfig, manualPredicate));
        resilience4jConfigLogger.logRetryConfig(name, retry);
        return retry;
    }

    public TimeLimiter createTimeLimiter(String name, CustomTimeLimiterConfig customConfig) {
        var timeLimiter = createTimeLimiter(name, builder -> applyCustomTimeLimiterConfig(builder, customConfig));
        resilience4jConfigLogger.logTimeLimiterConfig(name, timeLimiter);
        return timeLimiter;
    }

    private void applyCustomCircuitBreakerConfig(CircuitBreakerConfig.Builder builder, CustomCircuitBreakerConfig customConfig) {
        ofNullable(customConfig.getFailureRateThreshold()).ifPresent(builder::failureRateThreshold);

        ofNullable(customConfig.getSlowCallRateThreshold()).ifPresent(builder::slowCallRateThreshold);

        ofNullable(customConfig.getSlowCallDurationThreshold()).ifPresent(value -> builder.slowCallDurationThreshold(Duration.ofMillis(value)));

        ofNullable(customConfig.getWaitDurationInOpenState()).ifPresent(value -> builder.waitDurationInOpenState(Duration.ofMillis(value)));

        ofNullable(customConfig.getMinimumNumberOfCalls()).ifPresent(builder::minimumNumberOfCalls);

        ofNullable(customConfig.getSlidingWindowSize()).ifPresent(builder::slidingWindowSize);

        ofNullable(customConfig.getPermittedNumberOfCallsInHalfOpenState()).ifPresent(builder::permittedNumberOfCallsInHalfOpenState);

        ofNullable(customConfig.getAutomaticTransitionFromOpenToHalfOpenEnabled()).ifPresent(builder::automaticTransitionFromOpenToHalfOpenEnabled);

        if (customConfig.getRecordExceptions() != null && !customConfig.getRecordExceptions().isEmpty()) {
            var exceptionClasses = exceptionClassConverter.convertToClasses(customConfig.getRecordExceptions());
            builder.recordExceptions(exceptionClasses);
        }
    }

    private void applyCustomRetryConfig(RetryConfig.Builder builder, CustomRetryConfig customConfig, Predicate<Throwable> manualPredicate) {
        ofNullable(customConfig.getMaxAttempts()).ifPresent(builder::maxAttempts);

        if (Boolean.FALSE.equals(customConfig.getEnableExponentialBackoff())) {
            builder.intervalFunction(null);
            builder.intervalBiFunction(null);
            if (customConfig.getWaitDuration() != null) {
                builder.waitDuration(Duration.ofMillis(customConfig.getWaitDuration()));
            }
        } else if (Boolean.TRUE.equals(customConfig.getEnableExponentialBackoff())) {
            builder.intervalFunction(null);
            builder.intervalBiFunction(null);
            if (customConfig.getWaitDuration() != null && customConfig.getExponentialBackoffMultiplier() != null) {
                builder.intervalFunction(IntervalFunction.ofExponentialBackoff(
                    Duration.ofMillis(customConfig.getWaitDuration()),
                    customConfig.getExponentialBackoffMultiplier()));
            } else if (customConfig.getWaitDuration() != null) {
                builder.intervalFunction(IntervalFunction.ofExponentialBackoff(
                    Duration.ofMillis(customConfig.getWaitDuration()),
                    2.0));
            } else {
                builder.intervalFunction(IntervalFunction.ofExponentialBackoff());
            }
        } else if (customConfig.getWaitDuration() != null) {
            Double multiplier;
            builder.intervalFunction(null);
            builder.intervalBiFunction(null);
            if (customConfig.getExponentialBackoffMultiplier() != null) {
                multiplier = customConfig.getExponentialBackoffMultiplier();
            } else {
                var defaultConfig = retryRegistry.getConfiguration(DEFAULT_CONFIG_NAME).orElse(retryRegistry.getDefaultConfig());
                IntervalBiFunction<Object> intervalBiFunction = defaultConfig.getIntervalBiFunction();

                multiplier = 2.0; // 기본값
                try {
                    // 첫 번째 레벨: arg$1 가져오기
                    Field arg1Field = intervalBiFunction.getClass().getDeclaredField("arg$1");
                    arg1Field.setAccessible(true);
                    Object arg1 = arg1Field.get(intervalBiFunction);

                    // 두 번째 레벨: arg$1 안의 arg$2 가져오기
                    Field arg2Field = arg1.getClass().getDeclaredField("arg$2");
                    arg2Field.setAccessible(true);
                    Object arg2 = arg2Field.get(arg1);

                    // 세 번째 레벨: arg$2 안의 arg$1 가져오기 (multiplier)
                    Field multiplierField = arg2.getClass().getDeclaredField("arg$1");
                    multiplierField.setAccessible(true);
                    multiplier = (Double) multiplierField.get(arg2);

                } catch (Exception e) {
                    log.warn("Cannot access nested multiplier field: {}", e.getMessage());
                }
            }

            builder.intervalFunction(IntervalFunction.ofExponentialBackoff(
                Duration.ofMillis(customConfig.getWaitDuration()),
                multiplier));
        }

        if (Boolean.TRUE.equals(customConfig.getManual()) && manualPredicate != null) {
            builder.retryOnResult(null);
            builder.retryExceptions(null);
            builder.retryOnException(null);
            builder.retryOnException(manualPredicate);
        } else if (!Boolean.TRUE.equals(customConfig.getManual())) {
            if (customConfig.getRetryExceptions() != null && !customConfig.getRetryExceptions().isEmpty()) {
                builder.retryOnException(null);
                var exceptionClasses = exceptionClassConverter.convertToClasses(customConfig.getRetryExceptions());
                builder.retryExceptions(exceptionClasses);
            }
            if (customConfig.getIgnoreExceptions() != null && !customConfig.getIgnoreExceptions().isEmpty()) {
                builder.retryOnException(null);
                var exceptionClasses = exceptionClassConverter.convertToClasses(customConfig.getIgnoreExceptions());
                builder.ignoreExceptions(exceptionClasses);
            }
        }
    }

    private void applyCustomTimeLimiterConfig(TimeLimiterConfig.Builder builder, CustomTimeLimiterConfig customConfig) {
        ofNullable(customConfig.getTimeoutDuration()).ifPresent(value -> builder.timeoutDuration(Duration.ofMillis(value)));
        ofNullable(customConfig.getCancelRunningFuture()).ifPresent(builder::cancelRunningFuture);
    }
}
