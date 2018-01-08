package com.system.design.tinyurl.domain.url;

import java.util.Optional;

public interface TinyUrlRepository {

    TinyUrlId nextIdentity();

    void save(TinyUrl tinyUrl);

    Optional<TinyUrl> getById(TinyUrlId id);
}
