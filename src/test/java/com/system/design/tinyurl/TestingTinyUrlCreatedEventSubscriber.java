package com.system.design.tinyurl;

import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.Subscriber;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;

public class TestingTinyUrlCreatedEventSubscriber implements Subscriber {

    private TinyUrlCreatedEvent tinyUrlCreatedEvent;

    @Override
    public void receive(DomainEvent event) {
        this.tinyUrlCreatedEvent = (TinyUrlCreatedEvent) event;
    }

    public TinyUrlCreatedEvent event() {
        return tinyUrlCreatedEvent;
    }
}
