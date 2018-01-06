package com.system.design.tinyurl.infrastructure.event;

import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.DomainEventsPublisher;
import com.system.design.tinyurl.domain.event.Subscriber;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.singletonList;

public class KafkaDomainEventsPublisher implements DomainEventsPublisher {

    private static final String TOPIC = "tinyurl-topic";
    private final String bootstrapServers;
    private final Producer<Long, String> producer;
    private final Consumer<Long, String> consumer;
    private final ExecutorService executorService;

    private List<Subscriber> subscribers = new ArrayList<>();

    public KafkaDomainEventsPublisher(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
        this.consumer = createConsumer(bootstrapServers);
        this.producer = createProducer(bootstrapServers);
        this.executorService = Executors.newFixedThreadPool(5);
        this.consumer.subscribe(singletonList(TOPIC));
        executorService.execute(() -> {
            try {
                boolean running = true;
                while (running) {
                    ConsumerRecords<Long, String> records = consumer.poll(1000);
                    for (ConsumerRecord<Long, String> record : records)
                        System.out.println(record.offset() + ": " + record.value());
                }
            } finally {
                consumer.close();
            }
        });
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void publish(DomainEvent event) {
        producer.send(new ProducerRecord<>(TOPIC, new Random().nextLong(), event.toString()),
                (metadata, exception) -> System.out.println("completed: " + metadata.topic() + " " + metadata.offset()));
    }

    private Producer<Long, String> createProducer(String bootstrapServers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "TinyUrlEventsProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    private Consumer<Long, String> createConsumer(String bootstrapServers) {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                "TinyUrlEventsConsumer" + new Random().nextInt());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }
}
