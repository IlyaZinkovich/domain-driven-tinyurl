package com.system.design.tinyurl;

import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.infrastructure.event.KafkaDomainEventsPublisher;
import info.batey.kafka.unit.KafkaUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class KafkaDomainEventsPublisherIntegrationTest {

    private static final String kafkaConnection = "localhost:5000";
    private static final String zookeeperConnection = "localhost:5001";
    private static KafkaUnit kafkaUnitServer = new KafkaUnit(zookeeperConnection, kafkaConnection);

    @BeforeClass
    public static void setup() {
        kafkaUnitServer.startup();
        kafkaUnitServer.createTopic("tinyurl-topic");
    }

    @AfterClass
    public static void shutdown() {
        kafkaUnitServer.shutdown();
    }

    @Test
    public void testKafkaDomainEventsPublisher() throws InterruptedException, TimeoutException {
        DomainEventsPublisher domainEventsPublisher = new KafkaDomainEventsPublisher(kafkaConnection);
        domainEventsPublisher.subscribe(System.out::println);
        domainEventsPublisher.publish(new TinyUrlCreatedEvent(new TinyUrlId("uuid"), "http://some.url", "hash"));
        final List<String> messages = kafkaUnitServer.readMessages("tinyurl-topic", 1);
        messages.forEach(System.out::println);
        Thread.sleep(10000);
    }
}
