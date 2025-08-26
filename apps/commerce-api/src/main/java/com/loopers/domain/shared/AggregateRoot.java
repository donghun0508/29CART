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

    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    public boolean hasDomainEvents() {
        return !this.domainEvents.isEmpty();
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
