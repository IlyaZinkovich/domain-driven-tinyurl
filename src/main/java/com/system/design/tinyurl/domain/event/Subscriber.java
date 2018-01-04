package com.system.design.tinyurl.domain.event;

public interface Subscriber {

    void receive(DomainEvent event);
}
