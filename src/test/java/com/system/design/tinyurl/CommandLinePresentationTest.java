package com.system.design.tinyurl;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.hash.HashGenerator;
import com.system.design.tinyurl.infrastructure.cache.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.InMemoryDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.hash.md5.MD5HashGenerator;
import com.system.design.tinyurl.infrastructure.url.InMemoryTinyUrlRepository;
import com.system.design.tinyurl.presentation.CommandLinePresentation;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class CommandLinePresentationTest {

    @Test
    public void testPresentation() {
        final DomainEventsPublisher eventsPublisher = new InMemoryDomainEventsPublisher();
        final CacheService cacheService = new CacheService(new InMemoryUrlCache(), eventsPublisher);
        final HashGenerator hashGenerator = new MD5HashGenerator();
        final TinyUrlService tinyUrlService = new TinyUrlService(new InMemoryTinyUrlRepository(),
                hashGenerator, eventsPublisher);
        final String url = "http://some.url";
        final String hashedUrl = hashGenerator.hash(url);
        final Iterator<String> cmdInputIterator = Arrays.asList("create", url, "get", hashedUrl, "exit").iterator();
        final TestingStringOutputConsumer outputConsumer = new TestingStringOutputConsumer();
        new CommandLinePresentation(eventsPublisher, cacheService, tinyUrlService, cmdInputIterator, outputConsumer)
                .present();
        final String tinyUrlCreationOutput = outputConsumer.output().get(0);
        final String generatedUrlHash = tinyUrlCreationOutput.split(", Hash: ")[1];
        assertEquals(hashedUrl, generatedUrlHash);
        final String getUrlOutput = outputConsumer.output().get(1);
        assertEquals(url, getUrlOutput);
    }
}
