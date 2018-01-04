package com.system.design.tinyurl.domain.url;

import java.util.Objects;

public class TinyUrlId {

    private String uuid;

    public TinyUrlId(String uuid) {
        this.uuid = uuid;
    }

    public String uuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TinyUrlId tinyUrlId = (TinyUrlId) o;
        return Objects.equals(uuid, tinyUrlId.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
