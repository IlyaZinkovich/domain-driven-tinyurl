package com.system.design.tinyurl.domain.event;

public interface DomainEventsPublisher {

    void publish(DomainEvent domainEvent);
}
