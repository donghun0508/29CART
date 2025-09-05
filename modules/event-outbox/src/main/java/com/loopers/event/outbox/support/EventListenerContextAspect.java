package com.loopers.event.outbox.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EventListenerContextAspect {

    private final EventPropagation propagation;

    public EventListenerContextAspect(EventPropagation propagation) {
        this.propagation = propagation;
    }

    @Around("@annotation(com.loopers.event.outbox.support.SagaTrace)")
    public Object wrapListener(ProceedingJoinPoint pjp) throws Throwable {
        EventEnvelope<?> envelope = null;
        for (Object arg : pjp.getArgs()) {
            if (arg instanceof EventEnvelope<?> e) {
                envelope = e;
                break;
            }
        }
        if (envelope != null) {
            try (var ignored = propagation.open(envelope)) {
                return pjp.proceed();
            }
        }
        return pjp.proceed();
    }
}
