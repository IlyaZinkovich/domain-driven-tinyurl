package com.system.design.tinyurl;

import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;

import java.util.function.Consumer;

public class TestingTinyUrlCreatedEventConsumer implements Consumer<DomainEvent> {

    private TinyUrlCreatedEvent tinyUrlCreatedEvent;

    public TinyUrlCreatedEvent event() {
        return tinyUrlCreatedEvent;
    }

    @Override
    public void accept(DomainEvent domainEvent) {
        this.tinyUrlCreatedEvent = (TinyUrlCreatedEvent) domainEvent;
    }
}
