package com.system.design.tinyurl.application.url.command;

public class CreateTinyUrlCommand {

    private String longUrl;

    public CreateTinyUrlCommand(String longUrl) {
        this.longUrl = longUrl;
    }

    public String longUrl() {
        return longUrl;
    }
}
