package com.system.design.tinyurl;

import com.system.design.tinyurl.application.cache.CacheService;
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
        String longUrl = "longUrl";
        tinyUrlService.createTinyUrl(new CreateTinyUrlCommand(longUrl));
        TinyUrlCreatedEvent tinyUrlCreatedEvent = subscriber.event();
        TinyUrl tinyUrlById = repository.getById(tinyUrlCreatedEvent.tinyUrlId());
        assertEquals(tinyUrlById.originalValue(), urlCache.get(tinyUrlById.tinyValue()));
    }
}
