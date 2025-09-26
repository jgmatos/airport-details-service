package com.sporty.airport.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.sporty.airport.properties.CacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfig {

	private final CacheProperties cacheProperties;

	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager(cacheProperties.getName());
		cacheManager.setCaffeine(
				Caffeine.newBuilder()
						.expireAfterWrite(Duration.ofHours(cacheProperties.getExpireHours()))
						.maximumSize(cacheProperties.getMaxSize())
						.recordStats()
		);
		cacheManager.setAsyncCacheMode(true);

		return cacheManager;
	}
}
