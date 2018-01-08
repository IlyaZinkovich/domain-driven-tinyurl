package com.system.design.tinyurl.domain.url;

import com.system.design.tinyurl.domain.hash.HashGenerator;

public class TinyUrlFactory {

    private final HashGenerator hashGenerator;

    public TinyUrlFactory(HashGenerator hashGenerator) {
        this.hashGenerator = hashGenerator;
    }

    public TinyUrl create(TinyUrlId tinyUrlId, String originalUrl) {
        return new TinyUrl(tinyUrlId, hashGenerator.hash(originalUrl), originalUrl);
    }
}
