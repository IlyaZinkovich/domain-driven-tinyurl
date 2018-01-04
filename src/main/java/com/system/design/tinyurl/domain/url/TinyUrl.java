package com.system.design.tinyurl.domain.url;

import java.util.Objects;

public class TinyUrl {

    private TinyUrlId id;
    private String tinyUrl;
    private String originalUrl;

    public TinyUrl(TinyUrlId id, String tinyUrl, String originalUrl) {
        this.id = id;
        this.tinyUrl = tinyUrl;
        this.originalUrl = originalUrl;
    }

    public TinyUrlId id() {
        return id;
    }

    public String tinyUrl() {
        return tinyUrl;
    }

    public String originalUrl() {
        return originalUrl;
    }
}
