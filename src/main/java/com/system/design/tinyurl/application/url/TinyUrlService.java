package com.system.design.tinyurl.application.url;

import com.system.design.tinyurl.application.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.application.url.query.TinyUrlByIdQuery;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.hash.HashGenerator;
import com.system.design.tinyurl.domain.url.TinyUrl;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;

public class TinyUrlService {

    private final TinyUrlRepository repository;
    private final HashGenerator hashGenerator;
    private final DomainEventsPublisher eventsPublisher;

    public TinyUrlService(TinyUrlRepository repository, HashGenerator hashGenerator,
                          DomainEventsPublisher eventsPublisher) {
        this.hashGenerator = hashGenerator;
        this.eventsPublisher = eventsPublisher;
        this.repository = repository;
    }

    public void createTinyUrl(CreateTinyUrlCommand command) {
        final TinyUrlId tinyUrlId = repository.nextIdentity();
        final String originalUrl = command.originalUrl();
        final String hash = hashGenerator.hash(originalUrl);
        final TinyUrl tinyUrl = new TinyUrl(tinyUrlId, hash, originalUrl);
        repository.save(tinyUrl);
        eventsPublisher.publish(new TinyUrlCreatedEvent(tinyUrlId, tinyUrl.originalUrl(), tinyUrl.urlHash()));
    }

    public TinyUrl findTinyUrlById(TinyUrlByIdQuery query) {
        return repository.getById(query.tinyUrlId());
    }
}
