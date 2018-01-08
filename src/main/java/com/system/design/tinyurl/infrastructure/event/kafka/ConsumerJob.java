package com.system.design.tinyurl.infrastructure.event.kafka;

import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.event.Subscriber;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsumerJob implements Runnable {

    private AtomicBoolean isRunning;
    private Consumer<Long, DomainEvent> consumer;
    private List<Subscriber> subscribers;

    public ConsumerJob(Consumer<Long, DomainEvent> consumer, List<Subscriber> subscribers) {
        this.isRunning = new AtomicBoolean(true);
        this.consumer = consumer;
        this.subscribers = subscribers;
    }

    @Override
    public void run() {
        try {
            while (isRunning.get()) {
                ConsumerRecords<Long, DomainEvent> records = consumer.poll(1000);
                for (ConsumerRecord<Long, DomainEvent> record : records) {
                    subscribers.forEach(subscriber -> {
                        final DomainEvent domainEvent = record.value();
                        subscriber.receive(domainEvent);
                    });
                }
                consumer.commitAsync();
            }
        } finally {
            consumer.close();
        }
    }

    public void stop() {
        isRunning.set(false);
    }
}