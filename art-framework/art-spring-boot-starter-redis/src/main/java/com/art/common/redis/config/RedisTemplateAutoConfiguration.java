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

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-04-15 23:28
 */
@EnableCaching
@AutoConfiguration
@AutoConfigureBefore(name = { "org.redisson.spring.starter.RedissonAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration",
		"com.art.common.mq.redis.core.RedisMQTemplate" })
public class RedisTemplateAutoConfiguration {

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
	public GenericObjectPoolConfig<LettucePoolingClientConfiguration> genericObjectPoolConfig() {
		return new GenericObjectPoolConfig<>();
	}

	@Primary
	@Bean
	public LettuceConnectionFactory redisTemplateLocal(
			GenericObjectPoolConfig<LettucePoolingClientConfiguration> genericObjectPoolConfig,
			RedisProperties redisProperties) {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
		configuration.setHostName(redisProperties.getHost());
		configuration.setPort(redisProperties.getPort());
		configuration.setDatabase(redisProperties.getDatabase());
		configuration.setPassword(redisProperties.getPassword());

		LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration
				.builder().poolConfig(genericObjectPoolConfig).commandTimeout(redisProperties.getTimeout());

		return new LettuceConnectionFactory(configuration, builder.build());
	}

	/**
	 * 创建 RedisTemplate 设置序列化方式
	 */
	@Primary
	@Bean
	@ConditionalOnClass(RedisOperations.class)
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
		// 创建 RedisTemplate 对象
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		// 设置 RedisConnection 工厂。
		template.setConnectionFactory(factory);

		// 默认使用了jdk的序列化方式，可读性差，我们使用 String 序列化方式，序列化 KEY 。
		template.setKeySerializer(RedisSerializer.string());
		template.setHashKeySerializer(RedisSerializer.string());

		// 使用 JSON 序列化方式（库是 Jackson ），序列化 VALUE 。
		template.setValueSerializer(RedisSerializer.json());
		template.setHashValueSerializer(RedisSerializer.json());

		return template;
	}

}
