package com.system.design.tinyurl.domain.event;

import java.util.function.Consumer;

public interface DomainEventsSubscriber {

    void subscribe(Consumer<DomainEvent> callback);

    void shutdown();
}
