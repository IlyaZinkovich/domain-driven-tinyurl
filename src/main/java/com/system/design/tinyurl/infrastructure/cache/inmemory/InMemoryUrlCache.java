package com.system.design.tinyurl.infrastructure.cache.inmemory;

import com.system.design.tinyurl.domain.cache.UrlCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUrlCache implements UrlCache {

    private final Map<String, String> cache = new HashMap<>();

    @Override
    public void put(String urlHash, String originalUrl) {
        cache.put(urlHash, originalUrl);
    }

    @Override
    public Optional<String> get(String urlHash) {
        return Optional.ofNullable(cache.get(urlHash));
    }
}
