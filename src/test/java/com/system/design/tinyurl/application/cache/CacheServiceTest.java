package com.system.design.tinyurl.application.cache;

import com.system.design.tinyurl.domain.cache.query.OriginalUrlByHashQuery;
import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.infrastructure.cache.inmemory.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventLog;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsSubscriber;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class CacheServiceTest {

    @Test
    public void testHandleTinyUrlCreated() {
        final InMemoryDomainEventLog eventLog = new InMemoryDomainEventLog();
        final DomainEventsSubscriber domainEventsSubscriber = new InMemoryDomainEventsSubscriber(eventLog);
        final UrlCache urlCache = new InMemoryUrlCache();
        final CacheService cacheService = new CacheService(urlCache, domainEventsSubscriber);
        final TinyUrlId id = new TinyUrlId("uuid");
        final String originalUrl = "originalUrl";
        final String urlHash = "urlHash";
        cacheService.handleTinyUrlCreated(new TinyUrlCreatedEvent(id, originalUrl, urlHash));
        assertEquals(Optional.of(originalUrl), urlCache.get(urlHash));
        domainEventsSubscriber.shutdown();
    }

    @Test
    public void testFindOriginalUrlByHash() {
        final InMemoryDomainEventLog eventLog = new InMemoryDomainEventLog();
        final DomainEventsSubscriber domainEventsSubscriber = new InMemoryDomainEventsSubscriber(eventLog);
        final UrlCache urlCache = new InMemoryUrlCache();
        final String urlHash = "urlHash";
        final String originalUrl = "originalUrl";
        urlCache.put(urlHash, originalUrl);
        final CacheService cacheService = new CacheService(urlCache, domainEventsSubscriber);
        final String originalUrlByHash = cacheService.getOriginalUrlByHash(new OriginalUrlByHashQuery(urlHash))
                .orElseThrow(AssertionError::new);
        assertEquals(originalUrl, originalUrlByHash);
        domainEventsSubscriber.shutdown();
    }
}
