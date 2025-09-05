package com.loopers.common.domain;

import ch.qos.logback.core.util.StringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.util.StringUtils;

@MappedSuperclass
public abstract class AggregateRoot extends BaseEntity {

    @Column(name = "business_id", nullable = false, updatable = false, unique = true)
    private final String businessId = UUID.randomUUID().toString();

    public String aggregateType() {
        return this.getClass().getSimpleName();
    }

    public String businessId() {
        if(StringUtils.hasText(businessId)) {
            return businessId;
        }
        return this.getId().toString();
    }

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerEvent(DomainEvent domainEvent) {
        this.domainEvents.add(domainEvent);
    }

    public List<DomainEvent> events() {
        return new ArrayList<>(this.domainEvents);
    }

    public void clear() {
        this.domainEvents.clear();
    }

    public boolean hasDomainEvents() {
        return !this.domainEvents.isEmpty();
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
