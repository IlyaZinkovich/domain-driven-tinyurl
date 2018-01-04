package com.system.design.tinyurl.domain.url;

import com.system.design.tinyurl.domain.event.DomainEvent;

public class TinyUrlCreatedEvent implements DomainEvent {

    private TinyUrlId tinyUrlId;
    private String tinyUrl;
    private String longUrl;

    public TinyUrlCreatedEvent(TinyUrlId tinyUrlId, String longUrl, String tinyUrl) {
        this.tinyUrlId = tinyUrlId;
        this.tinyUrl = tinyUrl;
        this.longUrl = longUrl;
    }

    public TinyUrlId tinyUrlId() {
        return tinyUrlId;
    }

    public String tinyUrl() {
        return tinyUrl;
    }

    public String longUrl() {
        return longUrl;
    }
}
