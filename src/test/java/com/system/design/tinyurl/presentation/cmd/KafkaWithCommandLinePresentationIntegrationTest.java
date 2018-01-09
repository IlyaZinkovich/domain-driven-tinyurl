package com.system.design.tinyurl.presentation.cmd;

import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import com.system.design.tinyurl.domain.hash.HashGenerator;
import com.system.design.tinyurl.domain.url.TinyUrlFactory;
import com.system.design.tinyurl.infrastructure.cache.inmemory.InMemoryUrlCache;
import com.system.design.tinyurl.infrastructure.event.kafka.KafkaDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.event.kafka.KafkaDomainEventsSubscriber;
import com.system.design.tinyurl.infrastructure.hash.md5.MD5HashGenerator;
import com.system.design.tinyurl.infrastructure.url.inmemory.InMemoryTinyUrlRepository;
import com.system.design.tinyurl.tools.TestingStringOutputConsumer;
import info.batey.kafka.unit.KafkaUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class KafkaWithCommandLinePresentationIntegrationTest {

    private static final String kafkaConnection = "localhost:5000";
    private static final String zookeeperConnection = "localhost:5001";
    private static KafkaUnit kafkaUnitServer = new KafkaUnit(zookeeperConnection, kafkaConnection);

    @BeforeClass
    public static void setup() {
        kafkaUnitServer.startup();
    }

    @AfterClass
    public static void shutdown() {
        kafkaUnitServer.shutdown();
    }

    @Test
    public void testKafkaDomainEventsPublisher() {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final DomainEventsSubscriber cacheDomainEventsSubscriber = new KafkaDomainEventsSubscriber(executorService, kafkaConnection);
        final DomainEventsSubscriber presentationDomainEventsSubscriber = new KafkaDomainEventsSubscriber(executorService, kafkaConnection);
        final DomainEventsPublisher domainEventsPublisher = new KafkaDomainEventsPublisher(kafkaConnection);
        final CacheService cacheService = new CacheService(new InMemoryUrlCache(), cacheDomainEventsSubscriber);
        final HashGenerator hashGenerator = new MD5HashGenerator();
        final TinyUrlFactory tinyUrlFactory = new TinyUrlFactory(hashGenerator);
        final TinyUrlService tinyUrlService = new TinyUrlService(new InMemoryTinyUrlRepository(),
                tinyUrlFactory, domainEventsPublisher);
        final String url = "http://some.url";
        final String hashedUrl = hashGenerator.hash(url);
        final Iterator<String> cmdInputIterator = Arrays.asList("create", url, "wait", "get", hashedUrl, "exit").iterator();
        final TestingStringOutputConsumer outputConsumer = new TestingStringOutputConsumer();
        new CommandLinePresentation(presentationDomainEventsSubscriber, cacheService, tinyUrlService, cmdInputIterator, outputConsumer)
                .present();
        final String tinyUrlCreationOutput = outputConsumer.output().get(0);
        final String generatedUrlHash = tinyUrlCreationOutput.split(", Hash: ")[1];
        assertEquals(hashedUrl, generatedUrlHash);
        final String getUrlOutput = outputConsumer.output().get(1);
        assertEquals(url, getUrlOutput);
        cacheDomainEventsSubscriber.shutdown();
        presentationDomainEventsSubscriber.shutdown();
    }
}
