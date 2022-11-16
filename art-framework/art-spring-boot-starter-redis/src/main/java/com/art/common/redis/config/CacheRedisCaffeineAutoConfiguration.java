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
import com.art.common.redis.cache.properties.CacheRedisCaffeineProperties;
import com.art.common.redis.cache.support.CacheMessageConsumer;
import com.art.common.redis.cache.support.RedisCaffeineCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author fxz
 */
@AutoConfiguration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheRedisCaffeineProperties.class)
public class CacheRedisCaffeineAutoConfiguration {

	@Autowired
	private CacheRedisCaffeineProperties cacheRedisCaffeineProperties;

	@Bean
	public RedisCaffeineCacheManager cacheManager(RedisTemplate redisTemplate, RedisMQTemplate redisMQTemplate) {
		return new RedisCaffeineCacheManager(cacheRedisCaffeineProperties, redisTemplate, redisMQTemplate);
	}

	@Bean
	public CacheMessageConsumer cacheMessageConsumer(RedisCaffeineCacheManager redisCaffeineCacheManager) {
		return new CacheMessageConsumer(redisCaffeineCacheManager);
	}

}
