package com.system.design.tinyurl.application.url;

import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.url.*;
import com.system.design.tinyurl.domain.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.domain.url.query.TinyUrlByIdQuery;

import javax.transaction.Transactional;
import java.util.Optional;

public class TinyUrlService {

    private final TinyUrlRepository repository;
    private final TinyUrlFactory tinyUrlFactory;
    private final DomainEventsPublisher domainEventsPublisher;

    public TinyUrlService(TinyUrlRepository repository, TinyUrlFactory tinyUrlFactory,
                          DomainEventsPublisher domainEventsPublisher) {
        this.tinyUrlFactory = tinyUrlFactory;
        this.domainEventsPublisher = domainEventsPublisher;
        this.repository = repository;
    }

    @Transactional
    public TinyUrlId createTinyUrl(CreateTinyUrlCommand command) {
        final TinyUrlId tinyUrlId = repository.nextIdentity();
        final TinyUrl tinyUrl = tinyUrlFactory.create(tinyUrlId, command.originalUrl());
        repository.save(tinyUrl);
        domainEventsPublisher.publish(new TinyUrlCreatedEvent(tinyUrlId, tinyUrl.originalUrl(), tinyUrl.urlHash()));
        return tinyUrlId;
    }

    @Transactional
    public Optional<TinyUrl> getTinyUrlById(TinyUrlByIdQuery query) {
        return repository.getById(query.tinyUrlId());
    }
}
