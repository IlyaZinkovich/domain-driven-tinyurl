package com.system.design.tinyurl.domain.cache;

public interface UrlCache {

    void put(String urlHash, String originalUrl);
    String get(String urlHash);
}
