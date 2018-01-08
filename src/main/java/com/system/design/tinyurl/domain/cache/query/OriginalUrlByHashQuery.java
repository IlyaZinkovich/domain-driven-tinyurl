package com.system.design.tinyurl.domain.cache.query;

public class OriginalUrlByHashQuery {

    private final String urlHash;

    public OriginalUrlByHashQuery(String urlHash) {
        this.urlHash = urlHash;
    }

    public String urlHash() {
        return urlHash;
    }
}
