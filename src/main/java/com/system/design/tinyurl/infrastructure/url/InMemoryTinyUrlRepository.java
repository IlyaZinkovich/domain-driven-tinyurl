package com.system.design.tinyurl.infrastructure.url;

import com.system.design.tinyurl.domain.url.TinyUrl;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryTinyUrlRepository implements TinyUrlRepository {

    private final Map<TinyUrlId, TinyUrl> tinyUrls = new HashMap<>();

    public TinyUrlId nextIdentity() {
        return new TinyUrlId(UUID.randomUUID().toString());
    }

    public void save(TinyUrl tinyUrl) {
        tinyUrls.put(tinyUrl.id(), tinyUrl);
    }

    public Optional<TinyUrl> getById(TinyUrlId id) {
        return Optional.ofNullable(tinyUrls.get(id));
    }
}
