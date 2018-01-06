package com.system.design.tinyurl.application.cache.query;

public class OriginalUrlByHashQuery {

    private String urlHash;

    public OriginalUrlByHashQuery(String urlHash) {
        this.urlHash = urlHash;
    }

    public String urlHash() {
        return urlHash;
    }
}
