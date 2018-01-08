package com.system.design.tinyurl.infrastructure.event.inmemory;

import com.system.design.tinyurl.domain.event.DomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InMemoryDomainEventLog {

    private List<DomainEvent> events = new ArrayList<>();
    private List<Consumer<DomainEvent>> callbacks = new ArrayList<>();

    public void append(DomainEvent event) {
        events.add(event);
        callbacks.forEach(callback -> callback.accept(event));
    }

    public void subscribe(Consumer<DomainEvent> callback) {
        callbacks.add(callback);
    }

    public void unsubscribe(Consumer<DomainEvent> callback) {
        callbacks.remove(callback);
    }
}