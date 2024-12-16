package com.example.demo.configurations;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
	@Bean
	CacheManager cacheManager() {

		return new ConcurrentMapCacheManager();

	}

	// @Bean
	// public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

	// 	return RedisCacheManager
	// 		.builder(redisConnectionFactory)
	// 		.cacheDefaults(RedisCacheConfiguration
	// 			.defaultCacheConfig()
	// 			// .entryTtl(Duration.ofMinutes(10))
	// 			.disableCachingNullValues())
	// 		.build();

	// }

}
