package com.linhnt.velaecommerce.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@EnableCaching
@Configuration
public class CacheConfig {

    @Value("${spring.redis.redisson.config:}")
    private String redissonConfig;

    @Bean(destroyMethod="shutdown")
    RedissonClient redisson() throws IOException {
        if (!StringUtils.hasText(redissonConfig)) {
            log.warn("No redisson cache config found, ignore to use redis cache!");
            return null;
        }
        Config config = Config.fromYAML(redissonConfig);
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        if (!StringUtils.hasText(redissonConfig)) {
            log.warn("No redisson cache config found, use ConcurrentMapCacheManager for default cache manager!");
            return new ConcurrentMapCacheManager();
        }
        return new RedissonSpringCacheManager(redissonClient);
    }
}
