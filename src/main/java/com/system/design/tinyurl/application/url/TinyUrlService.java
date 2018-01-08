package com.system.design.tinyurl.application.url;

import com.system.design.tinyurl.application.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.application.url.query.TinyUrlByIdQuery;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.hash.HashGenerator;
import com.system.design.tinyurl.domain.url.TinyUrl;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public class TinyUrlService {

    private final TinyUrlRepository repository;
    private final HashGenerator hashGenerator;
    private final DomainEventsPublisher domainEventsPublisher;

    public TinyUrlService(TinyUrlRepository repository, HashGenerator hashGenerator,
                          DomainEventsPublisher domainEventsPublisher) {
        this.hashGenerator = hashGenerator;
        this.domainEventsPublisher = domainEventsPublisher;
        this.repository = repository;
    }

    @Transactional
    public void createTinyUrl(CreateTinyUrlCommand command) {
        final TinyUrlId tinyUrlId = repository.nextIdentity();
        final String originalUrl = command.originalUrl();
        final String hash = hashGenerator.hash(originalUrl);
        final TinyUrl tinyUrl = new TinyUrl(tinyUrlId, hash, originalUrl);
        repository.save(tinyUrl);
        domainEventsPublisher.publish(new TinyUrlCreatedEvent(tinyUrlId, tinyUrl.originalUrl(), tinyUrl.urlHash()));
    }

    @Transactional
    public Optional<TinyUrl> findTinyUrlById(TinyUrlByIdQuery query) {
        return repository.getById(query.tinyUrlId());
    }
}
