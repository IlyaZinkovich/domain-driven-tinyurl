package com.system.design.tinyurl.domain.cache;

public interface UrlCache {

    void put(String tinyUrl, String longUrl);
    String get(String tinyUrl);
}
