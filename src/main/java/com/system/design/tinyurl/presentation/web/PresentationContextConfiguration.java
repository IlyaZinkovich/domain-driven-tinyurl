package com.system.design.tinyurl.presentation.web;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.domain.url.TinyUrlFactory;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;
import com.system.design.tinyurl.infrastructure.cache.inmemory.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventLog;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsSubscriber;
import com.system.design.tinyurl.infrastructure.hash.md5.MD5HashGenerator;
import com.system.design.tinyurl.infrastructure.url.hibernate.HibernatePersistenceContextConfiguration;
import com.system.design.tinyurl.infrastructure.url.hibernate.HibernateTinyUrlRepository;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HibernatePersistenceContextConfiguration.class)
public class PresentationContextConfiguration {

    @Bean
    public CacheService cacheService(InMemoryDomainEventLog eventLog, UrlCache urlCache) {
        return new CacheService(urlCache, new InMemoryDomainEventsSubscriber(eventLog));
    }

    @Bean
    public UrlCache urlCache() {
        return new InMemoryUrlCache();
    }

    @Bean
    public TinyUrlService tinyUrlService(TinyUrlRepository repository, TinyUrlFactory tinyUrlFactory,
                                         InMemoryDomainEventLog domainEventLog) {
        return new TinyUrlService(repository, tinyUrlFactory, new InMemoryDomainEventsPublisher(domainEventLog));
    }

    @Bean
    public TinyUrlRepository tinyUrlRepository(SessionFactory sessionFactory) {
        return new HibernateTinyUrlRepository(sessionFactory);
    }

    @Bean
    public TinyUrlFactory tinyUrlFactory() {
        return new TinyUrlFactory(new MD5HashGenerator());
    }

    @Bean
    public InMemoryDomainEventLog inMemoryDomainEventLog() {
        return new InMemoryDomainEventLog();
    }
}
