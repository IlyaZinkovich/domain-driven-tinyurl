package com.system.design.tinyurl.presentation.web.cache;

public class OriginalUrlResponse {

    private String originalUrl;

    public OriginalUrlResponse(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String originalUrl() {
        return originalUrl;
    }
}
