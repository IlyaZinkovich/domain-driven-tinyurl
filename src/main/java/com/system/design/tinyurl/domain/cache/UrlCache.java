package com.system.design.tinyurl.domain.cache;

import java.util.Optional;

public interface UrlCache {

    void put(String urlHash, String originalUrl);

    Optional<String> get(String urlHash);
}
