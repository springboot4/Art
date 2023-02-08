/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.art.common.redis.config;

import com.art.common.mq.redis.core.RedisMQTemplate;
import com.art.common.redis.core.cache.properties.CacheRedisCaffeineProperties;
import com.art.common.redis.core.cache.support.CacheMessageConsumer;
import com.art.common.redis.core.cache.support.RedisCaffeineCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 多级缓存默认关闭
 *
 * @author fxz
 */
@ConditionalOnProperty(prefix = "redis.cache.multi", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@AutoConfiguration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheRedisCaffeineProperties.class)
public class CacheRedisCaffeineAutoConfiguration {

	private final CacheRedisCaffeineProperties cacheRedisCaffeineProperties;

	@Bean
	public CacheManager cacheManager(RedisTemplate redisTemplate, RedisMQTemplate redisMQTemplate) {
		return new RedisCaffeineCacheManager(cacheRedisCaffeineProperties, redisTemplate, redisMQTemplate);
	}

	@Bean
	public CacheMessageConsumer cacheMessageConsumer(CacheManager cacheManager) {
		return new CacheMessageConsumer(cacheManager);
	}

}
