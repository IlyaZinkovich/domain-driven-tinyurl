package com.system.design.tinyurl.domain.event;

public abstract class DomainEvent {

    private final String type;

    protected DomainEvent(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
