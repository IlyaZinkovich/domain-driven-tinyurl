package com.system.design.tinyurl.infrastructure.event.kafka;

import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Properties;
import java.util.Random;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class KafkaDomainEventsPublisher implements DomainEventsPublisher {

    private final Producer<Long, DomainEvent> producer;

    public KafkaDomainEventsPublisher(String bootstrapServers) {
        this.producer = createProducer(bootstrapServers);
    }

    @Override
    public void publish(DomainEvent domainEvent) {
        producer.send(new ProducerRecord<>("tinyurl-topic", new Random().nextLong(), domainEvent));
    }

    private Producer<Long, DomainEvent> createProducer(String bootstrapServers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(CLIENT_ID_CONFIG, "TinyUrlEventsProducer-" + new Random().nextInt());
        props.put(KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, DomainEventSerializer.class.getName());
        return new KafkaProducer<>(props);
    }
}
