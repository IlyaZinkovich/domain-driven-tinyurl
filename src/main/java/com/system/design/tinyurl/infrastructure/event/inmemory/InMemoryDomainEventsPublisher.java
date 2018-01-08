package com.system.design.tinyurl.infrastructure.event.inmemory;

import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;

public class InMemoryDomainEventsPublisher implements DomainEventsPublisher {

    private final InMemoryDomainEventLog eventLog;

    public InMemoryDomainEventsPublisher(InMemoryDomainEventLog eventLog) {
        this.eventLog = eventLog;
    }

    @Override
    public void publish(DomainEvent domainEvent) {
        eventLog.append(domainEvent);
    }
}
