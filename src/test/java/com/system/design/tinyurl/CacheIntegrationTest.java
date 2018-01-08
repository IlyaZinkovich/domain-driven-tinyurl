package com.system.design.tinyurl;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.domain.cache.query.OriginalUrlByHashQuery;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.url.TinyUrlFactory;
import com.system.design.tinyurl.domain.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import com.system.design.tinyurl.domain.hash.HashGenerator;
import com.system.design.tinyurl.domain.url.TinyUrl;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;
import com.system.design.tinyurl.infrastructure.cache.inmemory.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventLog;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsSubscriber;
import com.system.design.tinyurl.infrastructure.hash.md5.MD5HashGenerator;
import com.system.design.tinyurl.infrastructure.url.inmemory.InMemoryTinyUrlRepository;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheIntegrationTest {

    @Test
    public void testTinyUrlCreatedCaches() {
        final InMemoryDomainEventLog eventLog = new InMemoryDomainEventLog();
        final DomainEventsSubscriber domainEventsSubscriber = new InMemoryDomainEventsSubscriber(eventLog);
        final DomainEventsPublisher domainEventsPublisher = new InMemoryDomainEventsPublisher(eventLog);
        final TinyUrlRepository repository = new InMemoryTinyUrlRepository();
        final HashGenerator hashGenerator = new MD5HashGenerator();
        final TinyUrlFactory tinyUrlFactory = new TinyUrlFactory(hashGenerator);
        final TinyUrlService tinyUrlService = new TinyUrlService(repository, tinyUrlFactory, domainEventsPublisher);
        final UrlCache urlCache = new InMemoryUrlCache();
        final CacheService cacheService = new CacheService(urlCache, domainEventsSubscriber);
        final TestingTinyUrlCreatedEventConsumer subscriber = new TestingTinyUrlCreatedEventConsumer();
        domainEventsSubscriber.subscribe(subscriber);
        final String originalUrl = "originalUrl";
        tinyUrlService.createTinyUrl(new CreateTinyUrlCommand(originalUrl));
        final TinyUrlCreatedEvent tinyUrlCreatedEvent = subscriber.event();
        final TinyUrl tinyUrlById = repository.getById(tinyUrlCreatedEvent.tinyUrlId()).orElseThrow(AssertionError::new);
        assertEquals(tinyUrlById.originalUrl(), urlCache.get(tinyUrlById.urlHash()));
        final String cachedOriginalUrl = cacheService.findOriginalUrlByHash(new OriginalUrlByHashQuery(tinyUrlById.urlHash()));
        assertEquals(tinyUrlById.originalUrl(), cachedOriginalUrl);
        domainEventsSubscriber.shutdown();
    }
}
