package com.system.design.tinyurl.domain.url;

import com.system.design.tinyurl.domain.event.DomainEvent;

import java.util.Objects;

public class TinyUrlCreatedEvent extends DomainEvent {

    private final TinyUrlId tinyUrlId;
    private final String urlHash;
    private final String originalUrl;

    public TinyUrlCreatedEvent(TinyUrlId tinyUrlId, String originalUrl, String urlHash) {
        super("TinyUrlCreated");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TinyUrlCreatedEvent that = (TinyUrlCreatedEvent) o;
        return Objects.equals(tinyUrlId, that.tinyUrlId) &&
                Objects.equals(urlHash, that.urlHash) &&
                Objects.equals(originalUrl, that.originalUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tinyUrlId, urlHash, originalUrl);
    }
}
