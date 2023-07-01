/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.redis.starter;

import com.art.redis.common.event.consume.KeyDeletedEventMessageConsume;
import com.art.redis.common.event.consume.KeyExpiredEventMessageConsume;
import com.art.redis.common.event.consume.KeySetEventMessageConsume;
import com.art.redis.common.event.listener.AbstractKeyDeletedEventMessageListener;
import com.art.redis.common.event.listener.AbstractKeyExpiredEventMessageListener;
import com.art.redis.common.event.listener.AbstractKeySetEventMessageListener;
import com.art.redis.sdk.event.DefaultKeyDeletedEventMessageListener;
import com.art.redis.sdk.event.DefaultKeyExpiredEventMessageListener;
import com.art.redis.sdk.event.DefaultKeySetEventMessageListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2023/1/15 21:26
 */
@AutoConfiguration
public class RedisKeyEventAutoConfiguration {

	@ConditionalOnProperty(prefix = "redis.key.del", name = "enabled", havingValue = "true")
	public static class RedisKeyDeletedEventConfiguration {

		@Bean(name = "keyDeleteEventRedisMessageListenerContainer")
		@ConditionalOnMissingBean(name = "keyDeleteEventRedisMessageListenerContainer")
		public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
			RedisMessageListenerContainer container = new RedisMessageListenerContainer();
			container.setConnectionFactory(connectionFactory);
			return container;
		}

		@Bean(name = "keyDeletedEventMessageListener")
		@ConditionalOnMissingBean(name = "keyDeletedEventMessageListener")
		public AbstractKeyDeletedEventMessageListener keyDeletedEventMessageListener(@Qualifier(
				value = "keyDeleteEventRedisMessageListenerContainer") RedisMessageListenerContainer listenerContainer,
				ObjectProvider<List<KeyDeletedEventMessageConsume>> keyDeletedEventMessageConsume) {
			return new DefaultKeyDeletedEventMessageListener(listenerContainer, keyDeletedEventMessageConsume);
		}

	}

	@ConditionalOnProperty(prefix = "redis.key.set", name = "enabled", havingValue = "true")
	public static class RedisKeySetEventConfiguration {

		@Bean(name = "keySetEventRedisMessageListenerContainer")
		@ConditionalOnMissingBean(name = "keySetEventRedisMessageListenerContainer")
		public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
			RedisMessageListenerContainer container = new RedisMessageListenerContainer();
			container.setConnectionFactory(connectionFactory);
			return container;
		}

		@Bean(name = "keySetEventMessageListener")
		@ConditionalOnMissingBean(name = "keySetEventMessageListener")
		public AbstractKeySetEventMessageListener keySetEventMessageListener(@Qualifier(
				value = "keySetEventRedisMessageListenerContainer") RedisMessageListenerContainer listenerContainer,
				ObjectProvider<List<KeySetEventMessageConsume>> keySetEventMessageConsumes) {
			return new DefaultKeySetEventMessageListener(listenerContainer, keySetEventMessageConsumes);
		}

	}

	@ConditionalOnProperty(prefix = "redis.key.expired", name = "enabled", havingValue = "true")
	public static class RedisKeyExpiredEventConfiguration {

		@Bean(name = "keyExpiredEventRedisMessageListenerContainer")
		@ConditionalOnMissingBean(name = "keyExpiredEventRedisMessageListenerContainer")
		public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
			RedisMessageListenerContainer container = new RedisMessageListenerContainer();
			container.setConnectionFactory(connectionFactory);
			return container;
		}

		@Bean(name = "keyExpiredEventMessageListener")
		@ConditionalOnMissingBean(name = "keyExpiredEventMessageListener")
		public AbstractKeyExpiredEventMessageListener keyExpiredEventMessageListener(@Qualifier(
				value = "keyExpiredEventRedisMessageListenerContainer") RedisMessageListenerContainer listenerContainer,
				ObjectProvider<List<KeyExpiredEventMessageConsume>> keyExpiredEventMessageConsumes) {
			return new DefaultKeyExpiredEventMessageListener(listenerContainer, keyExpiredEventMessageConsumes);
		}

	}

}
