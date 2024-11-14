package com.example.demo.configurations;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableCaching
public class CacheConfig {
	@Bean
	@Primary
	public CacheManager cacheManager() {

		return new ConcurrentMapCacheManager();

	}

	@Bean
	// @Primary
	public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

		return RedisCacheManager
			.builder(redisConnectionFactory)
			.cacheDefaults(RedisCacheConfiguration
				.defaultCacheConfig()
				// .entryTtl(Duration.ofMinutes(10))
				.disableCachingNullValues())
			.build();

	}

}
