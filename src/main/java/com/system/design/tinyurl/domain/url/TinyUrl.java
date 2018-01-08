package com.system.design.tinyurl.domain.url;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TinyUrl tinyUrl = (TinyUrl) o;
        return Objects.equals(id, tinyUrl.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
