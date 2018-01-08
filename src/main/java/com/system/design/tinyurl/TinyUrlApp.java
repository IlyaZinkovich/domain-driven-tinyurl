package com.system.design.tinyurl;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import com.system.design.tinyurl.domain.hash.HashGenerator;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;
import com.system.design.tinyurl.infrastructure.cache.inmemory.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventLog;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsSubscriber;
import com.system.design.tinyurl.infrastructure.hash.md5.MD5HashGenerator;
import com.system.design.tinyurl.infrastructure.url.inmemory.InMemoryTinyUrlRepository;
import com.system.design.tinyurl.presentation.CommandLinePresentation;

import java.util.Scanner;

public class TinyUrlApp {

    public static void main(String[] args) {
        final InMemoryDomainEventLog eventLog = new InMemoryDomainEventLog();
        final DomainEventsSubscriber cacheDomainEventsSubscriber = new InMemoryDomainEventsSubscriber(eventLog);
        final DomainEventsSubscriber presentationDomainEventsSubscriber = new InMemoryDomainEventsSubscriber(eventLog);
        final DomainEventsPublisher domainEventsPublisher = new InMemoryDomainEventsPublisher(eventLog);
        final UrlCache urlCache = new InMemoryUrlCache();
        final CacheService cacheService = new CacheService(urlCache, cacheDomainEventsSubscriber);
        final TinyUrlRepository tinyUrlRepository = new InMemoryTinyUrlRepository();
        final HashGenerator hashGenerator = new MD5HashGenerator();
        final TinyUrlService tinyUrlService = new TinyUrlService(tinyUrlRepository, hashGenerator, domainEventsPublisher);
        final Scanner cmdInputScanner = new Scanner(System.in);
        new CommandLinePresentation(presentationDomainEventsSubscriber, cacheService, tinyUrlService,
                cmdInputScanner, System.out::println)
                .present();
    }
}
