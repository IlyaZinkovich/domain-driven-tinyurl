package com.system.design.tinyurl.infrastructure.event.json;

import com.google.gson.*;
import com.system.design.tinyurl.domain.event.DomainEvent;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;

import java.lang.reflect.Type;

public class DomainEventJsonAdapter implements JsonDeserializer<DomainEvent>, JsonSerializer<DomainEvent> {

    @Override
    public DomainEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final JsonObject rootJsonObject = json.getAsJsonObject();
        final String eventType = rootJsonObject.get("type").getAsString();
        if ("TinyUrlCreated".equals(eventType)) {
            final String tinyUrlIdUUID = rootJsonObject.get("tinyUrlId").getAsString();
            final String originalUrl = rootJsonObject.get("originalUrl").getAsString();
            final String urlHash = rootJsonObject.get("urlHash").getAsString();
            return new TinyUrlCreatedEvent(new TinyUrlId(tinyUrlIdUUID), originalUrl, urlHash);
        }
        throw new UnsupportedDomainEventTypeException(eventType);
    }

    @Override
    public JsonElement serialize(DomainEvent source, Type typeOfSource, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("type", new JsonPrimitive(source.type()));
        if (source instanceof TinyUrlCreatedEvent) {
            TinyUrlCreatedEvent event = (TinyUrlCreatedEvent) source;
            jsonObject.add("tinyUrlId", new JsonPrimitive(event.tinyUrlId().uuid()));
            jsonObject.add("originalUrl", new JsonPrimitive(event.originalUrl()));
            jsonObject.add("urlHash", new JsonPrimitive(event.urlHash()));
            return jsonObject;
        }
        throw new UnsupportedDomainEventTypeException(source.type());
    }
}
