package com.system.design.tinyurl.domain.url;

import com.system.design.tinyurl.domain.event.DomainEvent;

public class TinyUrlCreatedEvent implements DomainEvent {

    private TinyUrlId tinyUrlId;
    private String urlHash;
    private String originalUrl;

    public TinyUrlCreatedEvent(TinyUrlId tinyUrlId, String originalUrl, String urlHash) {
        this.tinyUrlId = tinyUrlId;
        this.urlHash = urlHash;
        this.originalUrl = originalUrl;
    }

    public TinyUrlId tinyUrlId() {
        return tinyUrlId;
    }

    public String urlHash() {
        return urlHash;
    }

    public String originalUrl() {
        return originalUrl;
    }
}
