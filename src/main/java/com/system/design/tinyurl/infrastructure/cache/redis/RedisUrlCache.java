package com.system.design.tinyurl.infrastructure.cache.redis;

import com.system.design.tinyurl.domain.cache.UrlCache;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisUrlCache implements UrlCache {

    private final RedisCommands<String, String> commands;

    public RedisUrlCache(StatefulRedisConnection<String, String> connection) {
        commands = connection.sync();
    }

    @Override
    public void put(String urlHash, String originalUrl) {
        commands.set(urlHash, originalUrl);
    }

    @Override
    public String get(String urlHash) {
        return commands.get(urlHash);
    }
}
