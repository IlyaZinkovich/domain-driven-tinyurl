package com.system.design.tinyurl.domain.url;

public class TinyUrl {

    private TinyUrlId id;
    private String tinyValue;
    private String originalValue;

    public TinyUrl(TinyUrlId id, String tinyValue, String originalValue) {
        this.id = id;
        this.tinyValue = tinyValue;
        this.originalValue = originalValue;
    }

    public TinyUrlId id() {
        return id;
    }

    public String tinyValue() {
        return tinyValue;
    }

    public String originalValue() {
        return originalValue;
    }
}
