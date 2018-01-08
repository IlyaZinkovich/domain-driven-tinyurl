package com.system.design.tinyurl.infrastructure.event.inmemory;

import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;

import java.util.function.Consumer;

public class InMemoryDomainEventsSubscriber implements DomainEventsSubscriber {

    private final InMemoryDomainEventLog eventLog;
    private Consumer<DomainEvent> callback;

    public InMemoryDomainEventsSubscriber(InMemoryDomainEventLog eventLog) {
        this.eventLog = eventLog;
    }

    @Override
    public void subscribe(Consumer<DomainEvent> callback) {
        this.callback = callback;
        eventLog.subscribe(callback);
    }

    @Override
    public void shutdown() {
        eventLog.unsubscribe(callback);
    }
}
