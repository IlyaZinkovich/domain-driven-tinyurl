package com.system.design.tinyurl;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.application.cache.query.LongUrlByTinyUrlQuery;
import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.infrastructure.cache.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.InMemoryDomainEventsPublisher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheServiceTest {

    @Test
    public void testHandleTinyUrlCreated() {
        DomainEventsPublisher eventsPublisher = new InMemoryDomainEventsPublisher();
        UrlCache urlCache = new InMemoryUrlCache();
        CacheService cacheService = new CacheService(eventsPublisher, urlCache);
        TinyUrlId id = new TinyUrlId("uuid");
        String longUrl = "longUrl";
        String tinyUrl = "tinyUrl";
        cacheService.handleTinyUrlCreated(new TinyUrlCreatedEvent(id, longUrl, tinyUrl));
        assertEquals(longUrl, urlCache.get(tinyUrl));
    }

    @Test
    public void testFindLongUrlByTinyUrl() {
        DomainEventsPublisher eventsPublisher = new InMemoryDomainEventsPublisher();
        UrlCache urlCache = new InMemoryUrlCache();
        String tinyUrl = "tinyUrl";
        String longUrl = "longUrl";
        urlCache.put(tinyUrl, longUrl);
        CacheService cacheService = new CacheService(eventsPublisher, urlCache);
        String longUrlByTinyUrl = cacheService.findLongUrlByTinyUrl(new LongUrlByTinyUrlQuery(tinyUrl));
        assertEquals(longUrl, longUrlByTinyUrl);
    }
}
