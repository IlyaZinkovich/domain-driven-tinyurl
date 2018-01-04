package com.system.design.tinyurl.infrastructure.event;

import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.event.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDomainEventsPublisher implements DomainEventsPublisher {

    private List<Subscriber> subscribers = new ArrayList<>();

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void publish(DomainEvent event) {
        subscribers.forEach(subscriber -> subscriber.receive(event));
    }
}
