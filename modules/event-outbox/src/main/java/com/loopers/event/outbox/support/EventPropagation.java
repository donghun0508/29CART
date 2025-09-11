package com.loopers.event.outbox.support;

import com.loopers.common.domain.DomainEvent;
import io.micrometer.tracing.BaggageInScope;
import io.micrometer.tracing.BaggageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventPropagation {

    private final BaggageManager baggage;

    public SafeCloseable open(EventEnvelope<? extends DomainEvent> envelope) {
        var md = envelope.metadata();
        String root = firstNonBlank(stripSaga(md.correlationId()), md.aggregateId());
        String corr = firstNonBlank(md.correlationId(), root != null ? "SAGA-" + root : null);

        final BaggageInScope bRoot = notBlank(root) ? baggage.createBaggageInScope("rootAggregateId", root) : null;
        final BaggageInScope bCorr = notBlank(corr) ? baggage.createBaggageInScope("correlationId", corr) : null;
        final BaggageInScope bCause = baggage.createBaggageInScope("causationId", md.eventId());
        final BaggageInScope bAgg = notBlank(root) ? baggage.createBaggageInScope("aggregateId", root) : null;

        return () -> {
            if (bAgg != null) {
                bAgg.close();
            }
            if (bCause != null) {
                bCause.close();
            }
            if (bCorr != null) {
                bCorr.close();
            }
            if (bRoot != null) {
                bRoot.close();
            }
        };
    }

    public String get(String key) {
        var b = baggage.getBaggage(key);
        return b != null ? b.get() : null;
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private String firstNonBlank(String a, String b) {
        return notBlank(a) ? a : (notBlank(b) ? b : null);
    }

    private String stripSaga(String corr) {
        return corr != null && corr.startsWith("SAGA-") && corr.length() > 5 ? corr.substring(5) : null;
    }
}
