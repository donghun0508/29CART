package com.loopers.domain.shared;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class AggregateRoot extends BaseEntity {

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerEvent(DomainEvent domainEvent) {
        this.domainEvents.add(domainEvent);
    }

    public List<DomainEvent> events() {
        List<DomainEvent> domainEvents = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return domainEvents;
    }

    public boolean hasDomainEvents() {
        return !this.domainEvents.isEmpty();
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
