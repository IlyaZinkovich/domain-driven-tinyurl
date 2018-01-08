package com.system.design.tinyurl;

import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.infrastructure.event.kafka.KafkaDomainEventsPublisher;
import com.system.design.tinyurl.infrastructure.event.kafka.KafkaDomainEventsSubscriber;
import info.batey.kafka.unit.KafkaUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Duration.FIVE_SECONDS;
import static org.junit.Assert.assertEquals;

public class KafkaDomainEventsPublisherIntegrationTest {

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
        final DomainEventsSubscriber domainEventsSubscriber = new KafkaDomainEventsSubscriber(executorService, kafkaConnection);
        final DomainEventsPublisher domainEventsPublisher = new KafkaDomainEventsPublisher(kafkaConnection);
        final TestingTinyUrlCreatedEventConsumer subscriber = new TestingTinyUrlCreatedEventConsumer();
        domainEventsSubscriber.subscribe(subscriber);
        final TinyUrlId tinyUrlId = new TinyUrlId("uuid");
        final String originalUrl = "http://some.url";
        final String urlHash = "urlHash";
        final TinyUrlCreatedEvent event = new TinyUrlCreatedEvent(tinyUrlId, originalUrl, urlHash);
        domainEventsPublisher.publish(event);
        await().atMost(FIVE_SECONDS).until(() -> subscriber.event() != null);
        assertEquals(event, subscriber.event());
        domainEventsSubscriber.shutdown();
    }
}
