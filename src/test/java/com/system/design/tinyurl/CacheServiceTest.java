package com.system.design.tinyurl;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.application.cache.query.OriginalUrlByHashQuery;
import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.infrastructure.cache.inmemory.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsPublisher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheServiceTest {

    @Test
    public void testHandleTinyUrlCreated() {
        DomainEventsPublisher eventsPublisher = new InMemoryDomainEventsPublisher();
        UrlCache urlCache = new InMemoryUrlCache();
        CacheService cacheService = new CacheService(urlCache, eventsPublisher);
        TinyUrlId id = new TinyUrlId("uuid");
        String originalUrl = "originalUrl";
        String urlHash = "urlHash";
        cacheService.handleTinyUrlCreated(new TinyUrlCreatedEvent(id, originalUrl, urlHash));
        assertEquals(originalUrl, urlCache.get(urlHash));
    }

    @Test
    public void testFindOriginalUrlByHash() {
        DomainEventsPublisher eventsPublisher = new InMemoryDomainEventsPublisher();
        UrlCache urlCache = new InMemoryUrlCache();
        String urlHash = "urlHash";
        String originalUrl = "originalUrl";
        urlCache.put(urlHash, originalUrl);
        CacheService cacheService = new CacheService(urlCache, eventsPublisher);
        String originalUrlByHash = cacheService.findOriginalUrlByHash(new OriginalUrlByHashQuery(urlHash));
        assertEquals(originalUrl, originalUrlByHash);
    }
}
