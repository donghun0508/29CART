package com.loopers.logging.support.analytics;

import java.time.Instant;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class EventTraceAspect {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Before("@annotation(eventTrace)")
    public void publishTrackingEvent(JoinPoint joinPoint, EventTrace eventTrace) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String threadName = Thread.currentThread().getName();

        TrackingEvent trackingEvent = TrackingEvent.builder()
            .methodName(signature.getMethod().getName())
            .className(signature.getDeclaringType().getSimpleName())
            .threadName(threadName)
            .timestamp(Instant.now())
            .phase("BEFORE")
            .arguments(Arrays.toString(args))
            .build();

        applicationEventPublisher.publishEvent(trackingEvent);
    }

    private String extractEventInfo(Object[] args) {
        for (Object arg : args) {
            if (arg != null && arg.getClass().getSimpleName().endsWith("Event")) {
                return arg.getClass().getSimpleName();
            }
        }
        return "UnknownEvent";
    }
}
