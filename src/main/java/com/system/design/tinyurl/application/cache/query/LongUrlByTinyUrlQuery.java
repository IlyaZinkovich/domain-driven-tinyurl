package com.system.design.tinyurl.application.cache.query;

public class LongUrlByTinyUrlQuery {

    private String tinyUrl;

    public LongUrlByTinyUrlQuery(String tinyUrl) {
        this.tinyUrl = tinyUrl;
    }

    public String tinyUrl() {
        return tinyUrl;
    }
}
