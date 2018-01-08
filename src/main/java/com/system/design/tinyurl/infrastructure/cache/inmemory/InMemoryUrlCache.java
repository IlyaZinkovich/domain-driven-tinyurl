package com.system.design.tinyurl.infrastructure.cache.inmemory;

import com.system.design.tinyurl.domain.cache.UrlCache;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUrlCache implements UrlCache {

    private final Map<String, String> cache = new HashMap<>();

    @Override
    public void put(String urlHash, String originalUrl) {
        cache.put(urlHash, originalUrl);
    }

    @Override
    public String get(String urlHash) {
        return cache.get(urlHash);
    }
}
