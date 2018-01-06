package com.system.design.tinyurl;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.application.cache.query.OriginalUrlByHashQuery;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.application.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.url.TinyUrl;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;
import com.system.design.tinyurl.infrastructure.cache.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.InMemoryDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.url.InMemoryTinyUrlRepository;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheIntegrationTest {

    @Test
    public void testTinyUrlCreatedCaches() {
        DomainEventsPublisher eventsPublisher = new InMemoryDomainEventsPublisher();
        TinyUrlRepository repository = new InMemoryTinyUrlRepository();
        TinyUrlService tinyUrlService = new TinyUrlService(repository, eventsPublisher);
        UrlCache urlCache = new InMemoryUrlCache();
        CacheService cacheService = new CacheService(urlCache, eventsPublisher);
        TestingTinyUrlCreatedEventSubscriber subscriber = new TestingTinyUrlCreatedEventSubscriber();
        eventsPublisher.subscribe(subscriber);
        String originalUrl = "originalUrl";
        tinyUrlService.createTinyUrl(new CreateTinyUrlCommand(originalUrl));
        TinyUrlCreatedEvent tinyUrlCreatedEvent = subscriber.event();
        TinyUrl tinyUrlById = repository.getById(tinyUrlCreatedEvent.tinyUrlId());
        assertEquals(tinyUrlById.originalUrl(), urlCache.get(tinyUrlById.urlHash()));
        final String cachedOriginalUrl = cacheService.findOriginalUrlByHash(new OriginalUrlByHashQuery(tinyUrlById.urlHash()));
        assertEquals(tinyUrlById.originalUrl(), cachedOriginalUrl);
    }
}
