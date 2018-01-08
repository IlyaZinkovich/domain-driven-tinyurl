package com.system.design.tinyurl;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import com.system.design.tinyurl.domain.hash.HashGenerator;
import com.system.design.tinyurl.infrastructure.cache.inmemory.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventLog;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsSubscriber;
import com.system.design.tinyurl.infrastructure.event.inmemory.InMemoryDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.hash.md5.MD5HashGenerator;
import com.system.design.tinyurl.infrastructure.url.inmemory.InMemoryTinyUrlRepository;
import com.system.design.tinyurl.presentation.CommandLinePresentation;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class CommandLinePresentationTest {

    @Test
    public void testPresentation() {
        final InMemoryDomainEventLog eventLog = new InMemoryDomainEventLog();
        final DomainEventsPublisher domainEventsPublisher = new InMemoryDomainEventsPublisher(eventLog);
        final DomainEventsSubscriber domainEventsSubscriber = new InMemoryDomainEventsSubscriber(eventLog);
        final CacheService cacheService = new CacheService(new InMemoryUrlCache(), domainEventsSubscriber);
        final HashGenerator hashGenerator = new MD5HashGenerator();
        final TinyUrlService tinyUrlService = new TinyUrlService(new InMemoryTinyUrlRepository(),
                hashGenerator, domainEventsPublisher);
        final String url = "http://some.url";
        final String hashedUrl = hashGenerator.hash(url);
        final Iterator<String> cmdInputIterator = Arrays.asList("create", url, "get", hashedUrl, "exit").iterator();
        final TestingStringOutputConsumer outputConsumer = new TestingStringOutputConsumer();
        new CommandLinePresentation(domainEventsSubscriber, cacheService, tinyUrlService, cmdInputIterator, outputConsumer)
                .present();
        final String tinyUrlCreationOutput = outputConsumer.output().get(0);
        final String generatedUrlHash = tinyUrlCreationOutput.split(", Hash: ")[1];
        assertEquals(hashedUrl, generatedUrlHash);
        final String getUrlOutput = outputConsumer.output().get(1);
        assertEquals(url, getUrlOutput);
        domainEventsSubscriber.shutdown();
    }
}
