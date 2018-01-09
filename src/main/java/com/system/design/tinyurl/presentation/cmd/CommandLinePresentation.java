package com.system.design.tinyurl.presentation.cmd;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.domain.cache.query.OriginalUrlByHashQuery;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;

import java.util.Iterator;
import java.util.function.Consumer;

public class CommandLinePresentation {

    private final CacheService cacheService;
    private final TinyUrlService tinyUrlService;
    private final Iterator<String> cmdInputScanner;
    private final Consumer<String> stringOutput;

    public CommandLinePresentation(DomainEventsSubscriber domainEventsSubscriber, CacheService cacheService,
                                   TinyUrlService tinyUrlService, Iterator<String> cmdInputScanner,
                                   Consumer<String> stringOutput) {
        this.cacheService = cacheService;
        this.tinyUrlService = tinyUrlService;
        this.cmdInputScanner = cmdInputScanner;
        this.stringOutput = stringOutput;
        domainEventsSubscriber.subscribe(this::handleTinyUrlCreation);
    }

    private void handleTinyUrlCreation(DomainEvent event) {
        final TinyUrlCreatedEvent tinyUrlCreatedEvent = (TinyUrlCreatedEvent) event;
        final String id = tinyUrlCreatedEvent.tinyUrlId().uuid();
        final String hash = tinyUrlCreatedEvent.urlHash();
        stringOutput.accept(String.format("Id: %s, Hash: %s", id, hash));
    }

    public void present() {
        while (true) {
            final String nextCommand = cmdInputScanner.next();
            if ("create".equals(nextCommand)) {
                final String url = cmdInputScanner.next();
                tinyUrlService.createTinyUrl(new CreateTinyUrlCommand(url));
            } else if ("get".equals(nextCommand)) {
                final String urlHash = cmdInputScanner.next();
                cacheService.getOriginalUrlByHash(new OriginalUrlByHashQuery(urlHash)).ifPresent(stringOutput::accept);
            } else if ("wait".equals(nextCommand)) {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else if ("exit".equals(nextCommand)) {
                break;
            }
        }
    }
}
