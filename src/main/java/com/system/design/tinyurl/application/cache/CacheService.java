package com.system.design.tinyurl.application.cache;

import com.system.design.tinyurl.domain.cache.query.OriginalUrlByHashQuery;
import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;

public class CacheService {

    private final UrlCache urlCache;

    public CacheService(UrlCache urlCache, DomainEventsSubscriber domainEventsSubscriber) {
        this.urlCache = urlCache;
        domainEventsSubscriber.subscribe(this::handleTinyUrlCreated);
    }

    public void handleTinyUrlCreated(DomainEvent event) {
        if (event instanceof TinyUrlCreatedEvent) {
            final TinyUrlCreatedEvent tinyUrlCreatedEvent = (TinyUrlCreatedEvent) event;
            urlCache.put(tinyUrlCreatedEvent.urlHash(), tinyUrlCreatedEvent.originalUrl());
        }
    }

    public String findOriginalUrlByHash(OriginalUrlByHashQuery query) {
        return urlCache.get(query.urlHash());
    }
}
