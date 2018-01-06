package com.system.design.tinyurl.domain.event;

public interface DomainEventsPublisher {

    void subscribe(Subscriber subscriber);

    void publish(DomainEvent event);
}
