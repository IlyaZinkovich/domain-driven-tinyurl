package com.system.design.tinyurl.application.url;

import com.system.design.tinyurl.application.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.application.url.query.TinyUrlByIdQuery;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.url.TinyUrl;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;

public class TinyUrlService {

    private final TinyUrlRepository repository;
    private final DomainEventsPublisher eventsPublisher;

    public TinyUrlService(TinyUrlRepository repository, DomainEventsPublisher eventsPublisher) {
        this.eventsPublisher = eventsPublisher;
        this.repository = repository;
    }

    public void createTinyUrl(CreateTinyUrlCommand command) {
        TinyUrlId tinyUrlId = repository.nextIdentity();
        String originalUrl = command.originalUrl();
        TinyUrl tinyUrl = new TinyUrl(tinyUrlId, originalUrl, originalUrl);
        repository.save(tinyUrl);
        eventsPublisher.publish(new TinyUrlCreatedEvent(tinyUrlId, originalUrl, tinyUrl.urlHash()));
    }

    public TinyUrl findTinyUrlById(TinyUrlByIdQuery query) {
        return repository.getById(query.tinyUrlId());
    }
}
