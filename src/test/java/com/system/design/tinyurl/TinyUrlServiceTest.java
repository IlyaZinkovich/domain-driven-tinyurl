package com.system.design.tinyurl;

import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.url.*;
import com.system.design.tinyurl.domain.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.domain.url.query.TinyUrlByIdQuery;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import com.system.design.tinyurl.domain.hash.HashGenerator;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventLog;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsSubscriber;
import com.system.design.tinyurl.infrastructure.hash.md5.MD5HashGenerator;
import com.system.design.tinyurl.infrastructure.url.inmemory.InMemoryTinyUrlRepository;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TinyUrlServiceTest {

    @Test
    public void testCreateTinyUrl() {
        final InMemoryDomainEventLog eventLog = new InMemoryDomainEventLog();
        final DomainEventsPublisher domainEventsPublisher = new InMemoryDomainEventsPublisher(eventLog);
        final DomainEventsSubscriber domainEventsSubscriber = new InMemoryDomainEventsSubscriber(eventLog);
        final TestingTinyUrlCreatedEventConsumer consumer = new TestingTinyUrlCreatedEventConsumer();
        domainEventsSubscriber.subscribe(consumer);
        final HashGenerator hashGenerator = new MD5HashGenerator();
        final TinyUrlRepository repository = new InMemoryTinyUrlRepository();
        final TinyUrlFactory tinyUrlFactory = new TinyUrlFactory(hashGenerator);
        final TinyUrlService service = new TinyUrlService(repository, tinyUrlFactory, domainEventsPublisher);
        final String originalUrl = "http://some.long/url";
        service.createTinyUrl(new CreateTinyUrlCommand(originalUrl));
        final TinyUrlCreatedEvent event = consumer.event();
        final TinyUrl tinyUrl = repository.getById(event.tinyUrlId()).orElseThrow(AssertionError::new);
        assertNotNull(tinyUrl);
        assertNotNull(tinyUrl.id());
        assertEquals(originalUrl, tinyUrl.originalUrl());
        assertEquals(hashGenerator.hash(originalUrl), tinyUrl.urlHash());
        domainEventsSubscriber.shutdown();
    }

    @Test
    public void testFindTinyUrlById() {
        final InMemoryDomainEventLog eventLog = new InMemoryDomainEventLog();
        final DomainEventsPublisher domainEventsPublisher = new InMemoryDomainEventsPublisher(eventLog);
        final TinyUrlRepository repository = new InMemoryTinyUrlRepository();
        final HashGenerator hashGenerator = new MD5HashGenerator();
        final TinyUrlFactory tinyUrlFactory = new TinyUrlFactory(hashGenerator);
        final TinyUrlService service = new TinyUrlService(repository, tinyUrlFactory, domainEventsPublisher);
        final TinyUrlId tinyUrlId = new TinyUrlId("uuid");
        final TinyUrl tinyUrl = new TinyUrl(tinyUrlId, "urlHash", "originalUrl");
        repository.save(tinyUrl);
        final TinyUrl tinyUrlById = service.findTinyUrlById(new TinyUrlByIdQuery(tinyUrlId)).orElseThrow(AssertionError::new);
        assertEquals(tinyUrl, tinyUrlById);
    }
}
