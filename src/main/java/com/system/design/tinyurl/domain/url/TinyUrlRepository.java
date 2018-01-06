package com.system.design.tinyurl.domain.url;

public interface TinyUrlRepository {

    TinyUrlId nextIdentity();

    void save(TinyUrl tinyUrl);

    TinyUrl getById(TinyUrlId id);
}
