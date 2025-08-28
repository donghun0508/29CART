package com.loopers.logging.tracking;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class EventTraceAspect {

    @Around("@annotation(com.loopers.logging.tracking.EventTrace)")
    public Object logEventExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String threadName = Thread.currentThread().getName();

        Object event = null;
        for (Object arg : args) {
            if (arg != null && arg.getClass().getSimpleName().endsWith("Event")) {
                event = arg;
                break;
            }
        }

        String eventType = event != null ? event.getClass().getSimpleName() : "UnknownEvent";
        String methodName = signature.getMethod().getName();

        log.info("Event [{}] triggered -> Handler: {} [Thread: {}]", eventType, methodName, threadName);

        try {
            Object result = joinPoint.proceed();
            log.info("Event [{}] processed successfully [Thread: {}]", eventType, threadName);
            return result;
        } catch (Exception e) {
            log.error("Event [{}] processing failed: {} [Thread: {}]", eventType, e.getMessage(), threadName);
            throw e;
        }
    }
}
