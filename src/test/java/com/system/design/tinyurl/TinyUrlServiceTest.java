package com.system.design.tinyurl;

import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.application.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.application.url.query.TinyUrlByIdQuery;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.hash.HashGenerator;
import com.system.design.tinyurl.domain.url.TinyUrl;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;
import com.system.design.tinyurl.infrastructure.event.InMemoryDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.hash.md5.MD5HashGenerator;
import com.system.design.tinyurl.infrastructure.url.InMemoryTinyUrlRepository;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TinyUrlServiceTest {

    @Test
    public void testCreateTinyUrl() {
        DomainEventsPublisher eventsPublisher = new InMemoryDomainEventsPublisher();
        TestingTinyUrlCreatedEventSubscriber subscriber = new TestingTinyUrlCreatedEventSubscriber();
        eventsPublisher.subscribe(subscriber);
        HashGenerator hashGenerator = new MD5HashGenerator();
        TinyUrlRepository repository = new InMemoryTinyUrlRepository();
        TinyUrlService service = new TinyUrlService(repository, hashGenerator, eventsPublisher);
        String originalUrl = "http://some.long/url";
        service.createTinyUrl(new CreateTinyUrlCommand(originalUrl));
        TinyUrlCreatedEvent event = subscriber.event();
        TinyUrl tinyUrl = repository.getById(event.tinyUrlId()).orElseThrow(AssertionError::new);
        assertNotNull(tinyUrl);
        assertNotNull(tinyUrl.id());
        assertEquals(originalUrl, tinyUrl.originalUrl());
        assertEquals(hashGenerator.hash(originalUrl), tinyUrl.urlHash());
    }

    @Test
    public void testFindTinyUrlById() {
        DomainEventsPublisher eventsPublisher = new InMemoryDomainEventsPublisher();
        TinyUrlRepository repository = new InMemoryTinyUrlRepository();
        HashGenerator hashGenerator = new MD5HashGenerator();
        TinyUrlService service = new TinyUrlService(repository, hashGenerator, eventsPublisher);
        TinyUrlId tinyUrlId = new TinyUrlId("uuid");
        TinyUrl tinyUrl = new TinyUrl(tinyUrlId, "urlHash", "originalUrl");
        repository.save(tinyUrl);
        TinyUrl tinyUrlById = service.findTinyUrlById(new TinyUrlByIdQuery(tinyUrlId)).orElseThrow(AssertionError::new);
        assertEquals(tinyUrl, tinyUrlById);
    }
}
