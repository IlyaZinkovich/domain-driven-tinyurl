package com.system.design.tinyurl.application.url.command;

public class CreateTinyUrlCommand {

    private String originalUrl;

    public CreateTinyUrlCommand(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String originalUrl() {
        return originalUrl;
    }
}
