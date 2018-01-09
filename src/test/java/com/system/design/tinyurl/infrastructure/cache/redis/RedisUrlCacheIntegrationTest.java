package com.system.design.tinyurl.infrastructure.cache.redis;

import com.system.design.tinyurl.domain.cache.UrlCache;
import com.system.design.tinyurl.infrastructure.cache.redis.RedisUrlCache;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class RedisUrlCacheIntegrationTest {

    private static RedisServer redisServer;

    @BeforeClass
    public static void setup() throws IOException {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterClass
    public static void tearDown() {
        redisServer.stop();
    }

    @Test
    public void test() {
        final RedisClient redisClient = RedisClient.create(RedisURI.builder().withHost("localhost").withPort(6379).build());
        final StatefulRedisConnection<String, String> connection = redisClient.connect();
        final UrlCache urlCache = new RedisUrlCache(connection);
        final String urlHash = "urlHash";
        final String originalUrl = "originalUrl";
        urlCache.put(urlHash, originalUrl);
        assertEquals(Optional.of(originalUrl), urlCache.get(urlHash));
        connection.close();
        redisClient.shutdown();
    }
}
