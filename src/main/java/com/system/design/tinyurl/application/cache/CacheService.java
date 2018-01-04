package com.system.design.tinyurl.application.cache;

import com.system.design.tinyurl.application.cache.query.LongUrlByTinyUrlQuery;
import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;

public class CacheService {

    private UrlCache urlCache;

    public CacheService(UrlCache urlCache, DomainEventsPublisher eventsPublisher) {
        this.urlCache = urlCache;
        eventsPublisher.subscribe(this::handleTinyUrlCreated);
    }

    public void handleTinyUrlCreated(DomainEvent event) {
        if (event instanceof TinyUrlCreatedEvent) {
            TinyUrlCreatedEvent tinyUrlCreatedEvent = (TinyUrlCreatedEvent) event;
            urlCache.put(tinyUrlCreatedEvent.tinyUrl(), tinyUrlCreatedEvent.longUrl());
        }
    }

    public String findLongUrlByTinyUrl(LongUrlByTinyUrlQuery query) {
        return urlCache.get(query.tinyUrl());
    }
}
