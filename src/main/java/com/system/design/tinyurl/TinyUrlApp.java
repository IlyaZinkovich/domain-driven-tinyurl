package com.system.design.tinyurl;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.hash.HashGenerator;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;
import com.system.design.tinyurl.infrastructure.cache.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.InMemoryDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.hash.md5.MD5HashGenerator;
import com.system.design.tinyurl.infrastructure.url.InMemoryTinyUrlRepository;
import com.system.design.tinyurl.presentation.CommandLinePresentation;

import java.util.Scanner;

public class TinyUrlApp {

    public static void main(String[] args) {
        final UrlCache urlCache = new InMemoryUrlCache();
        final DomainEventsPublisher eventsPublisher = new InMemoryDomainEventsPublisher();
        final CacheService cacheService = new CacheService(urlCache, eventsPublisher);
        final TinyUrlRepository tinyUrlRepository = new InMemoryTinyUrlRepository();
        final HashGenerator hashGenerator = new MD5HashGenerator();
        final TinyUrlService tinyUrlService = new TinyUrlService(tinyUrlRepository, hashGenerator, eventsPublisher);
        final Scanner cmdInputScanner = new Scanner(System.in);
        new CommandLinePresentation(eventsPublisher, cacheService, tinyUrlService, cmdInputScanner, System.out::println)
                .present();
    }
}
