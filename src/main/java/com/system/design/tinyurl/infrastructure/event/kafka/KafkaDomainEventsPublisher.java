package com.system.design.tinyurl.infrastructure.event.kafka;

import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.event.Subscriber;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.singletonList;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class KafkaDomainEventsPublisher implements DomainEventsPublisher {

    private static final String TOPIC = "tinyurl-topic";
    private final Producer<Long, DomainEvent> producer;
    private final Consumer<Long, DomainEvent> consumer;
    private final List<Subscriber> subscribers = new ArrayList<>();
    private final ConsumerJob consumerJob;

    public KafkaDomainEventsPublisher(String bootstrapServers) {
        this.consumer = createConsumer(bootstrapServers);
        this.producer = createProducer(bootstrapServers);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        this.consumer.subscribe(singletonList(TOPIC));
        this.consumerJob = new ConsumerJob(consumer, subscribers);
        executorService.execute(consumerJob);
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void publish(DomainEvent event) {
        producer.send(new ProducerRecord<>(TOPIC, new Random().nextLong(), event));
    }

    private Producer<Long, DomainEvent> createProducer(String bootstrapServers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(CLIENT_ID_CONFIG, "TinyUrlEventsProducer");
        props.put(KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, DomainEventSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    private Consumer<Long, DomainEvent> createConsumer(String bootstrapServers) {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(GROUP_ID_CONFIG, "TinyUrlEventsConsumer");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, DomainEventDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }

    public void shutdown() {
        consumerJob.stop();
    }
}
