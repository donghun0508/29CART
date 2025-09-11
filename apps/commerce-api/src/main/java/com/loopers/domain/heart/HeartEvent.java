package com.loopers.domain.heart;

import com.loopers.common.domain.DomainEvent;
import lombok.Getter;

public class HeartEvent {

    @Getter
    public static class HeartCreatedEvent implements DomainEvent {
        private final Long targetId;
        private final TargetType targetType;

        public HeartCreatedEvent(Heart heart) {
            this.targetId = heart.getTarget().targetId();
            this.targetType = heart.getTarget().targetType();
        }
    }

    @Getter
    public static class HeartRemovedEvent implements DomainEvent {
        private final Long targetId;
        private final TargetType targetType;

        public HeartRemovedEvent(Heart heart) {
            this.targetId = heart.getTarget().targetId();
            this.targetType = heart.getTarget().targetType();
        }
    }

}
