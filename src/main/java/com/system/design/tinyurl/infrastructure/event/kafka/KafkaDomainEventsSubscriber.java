package com.system.design.tinyurl.infrastructure.event.kafka;

import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.LongDeserializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Collections.singletonList;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

public class KafkaDomainEventsSubscriber implements DomainEventsSubscriber {

    private final Consumer<Long, DomainEvent> consumer;
    private final AtomicBoolean isRunning;
    private final ExecutorService executorService;
    private java.util.function.Consumer<DomainEvent> callback;

    public KafkaDomainEventsSubscriber(ExecutorService executorService, String bootstrapServers) {
        this.isRunning = new AtomicBoolean(true);
        this.executorService = executorService;
        this.consumer = createConsumer(bootstrapServers);
    }

    @Override
    public void subscribe(java.util.function.Consumer<DomainEvent> callback) {
        this.callback = callback;
        this.consumer.subscribe(singletonList("tinyurl-topic"));
        this.isRunning.set(true);
        this.executorService.submit(this::run);
    }

    @Override
    public void shutdown() {
        this.isRunning.set(false);
    }

    private void run() {
        try {
            while (isRunning.get()) {
                ConsumerRecords<Long, DomainEvent> records = consumer.poll(500);
                for (ConsumerRecord<Long, DomainEvent> record : records) {
                    callback.accept(record.value());
                }
                consumer.commitAsync();
            }
        } finally {
            consumer.close();
        }
    }

    private Consumer<Long, DomainEvent> createConsumer(String bootstrapServers) {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(GROUP_ID_CONFIG, "TinyUrlEventsConsumer-" + new Random().nextInt());
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, DomainEventDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }
}
