package com.system.design.tinyurl.domain.url;

public class TinyUrl {

    private final TinyUrlId id;
    private final String urlHash;
    private final String originalUrl;

    public TinyUrl(TinyUrlId id, String urlHash, String originalUrl) {
        this.id = id;
        this.urlHash = urlHash;
        this.originalUrl = originalUrl;
    }

    public TinyUrlId id() {
        return id;
    }

    public String urlHash() {
        return urlHash;
    }

    public String originalUrl() {
        return originalUrl;
    }
}
