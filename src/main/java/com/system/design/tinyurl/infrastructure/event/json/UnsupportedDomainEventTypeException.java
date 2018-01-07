package com.system.design.tinyurl.infrastructure.event.json;

class UnsupportedDomainEventTypeException extends RuntimeException {

    UnsupportedDomainEventTypeException(String eventType) {
        super(String.format("Event type %s is not supported", eventType));
    }
}
