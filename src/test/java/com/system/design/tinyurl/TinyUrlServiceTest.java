package com.system.design.tinyurl;

import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.application.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.application.url.query.TinyUrlByIdQuery;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.url.TinyUrl;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;
import com.system.design.tinyurl.infrastructure.event.InMemoryDomainEventsPublisher;
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
        TinyUrlRepository repository = new InMemoryTinyUrlRepository();
        TinyUrlService service = new TinyUrlService(repository, eventsPublisher);
        String longUrl = "http://some.long/url";
        service.createTinyUrl(new CreateTinyUrlCommand(longUrl));
        TinyUrlCreatedEvent event = subscriber.event();
        TinyUrl tinyUrl = repository.getById(event.tinyUrlId());
        assertNotNull(tinyUrl);
        assertNotNull(tinyUrl.id());
        assertEquals(longUrl, tinyUrl.originalValue());
        assertEquals(longUrl, tinyUrl.tinyValue());
    }

    @Test
    public void testFindTinyUrlById() {
        DomainEventsPublisher eventsPublisher = new InMemoryDomainEventsPublisher();
        TinyUrlRepository repository = new InMemoryTinyUrlRepository();
        TinyUrlService service = new TinyUrlService(repository, eventsPublisher);
        TinyUrlId tinyUrlId = new TinyUrlId("uuid");
        TinyUrl tinyUrl = new TinyUrl(tinyUrlId, "tinyUrl", "longUrl");
        repository.save(tinyUrl);
        TinyUrl tinyUrlById = service.findTinyUrlById(new TinyUrlByIdQuery(tinyUrlId));
        assertEquals(tinyUrl, tinyUrlById);
    }
}
