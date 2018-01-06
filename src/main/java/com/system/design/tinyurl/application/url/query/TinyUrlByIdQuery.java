package com.system.design.tinyurl.application.url.query;

import com.system.design.tinyurl.domain.url.TinyUrlId;

public class TinyUrlByIdQuery {

    private final TinyUrlId tinyUrlId;

    public TinyUrlByIdQuery(TinyUrlId tinyUrlId) {
        this.tinyUrlId = tinyUrlId;
    }

    public TinyUrlId tinyUrlId() {
        return tinyUrlId;
    }
}
