package com.system.design.tinyurl.infrastructure.event.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.infrastructure.event.json.DomainEventJsonAdapter;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.Charset;
import java.util.Map;

public class DomainEventDeserializer implements Deserializer<DomainEvent> {

    private static final Charset ENCODING = Charset.forName("UTF-8");

    private final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(DomainEvent.class, new DomainEventJsonAdapter())
            .create();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Does not require additional configuration
    }

    @Override
    public DomainEvent deserialize(String topic, byte[] data) {
        return gson.fromJson(new String(data, ENCODING), DomainEvent.class);
    }

    @Override
    public void close() {
        // Does not require to close any resources
    }
}
