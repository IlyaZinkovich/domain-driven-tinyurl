package com.system.design.tinyurl.infrastructure.cache;

import com.system.design.tinyurl.domain.cache.UrlCache;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUrlCache implements UrlCache {

    private Map<String, String> cache = new HashMap<>();

    @Override
    public void put(String tinyUrl, String longUrl) {
        cache.put(tinyUrl, longUrl);
    }

    @Override
    public String get(String tinyUrl) {
        return cache.get(tinyUrl);
    }
}
