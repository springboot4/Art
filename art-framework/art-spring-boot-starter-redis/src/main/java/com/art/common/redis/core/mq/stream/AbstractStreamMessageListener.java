/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.redis.core.mq.stream;

import cn.hutool.core.util.TypeUtil;
import com.art.common.jackson.util.JacksonUtil;
import com.art.common.redis.core.mq.client.RedisMQTemplate;
import com.art.common.redis.core.mq.interceptor.RedisMessageInterceptor;
import com.art.common.redis.core.mq.message.AbstractRedisMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Stream 消息监听器抽象类
 *
 * @param <T> 消息类型
 * @author fxz
 */
@SuppressWarnings("all")
@Setter
@Getter
@Slf4j
public abstract class AbstractStreamMessageListener<T extends AbstractStreamMessage>
		implements StreamListener<String, ObjectRecord<String, String>> {

	/**
	 * 消息类型
	 */
	private final Class<T> messageType;

	/**
	 * streamKey
	 */
	private final String streamKey;

	/**
	 * Redis 消费者分组
	 */
	@Value("${spring.application.name}")
	private String group;

	/**
	 * RedisMQTemplate
	 */
	private RedisMQTemplate redisMQTemplate;

	@SneakyThrows
	protected AbstractStreamMessageListener() {
		this.messageType = getMessageClass();
		this.streamKey = messageType.newInstance().getStreamKey();
	}

	@Override
	public void onMessage(ObjectRecord<String, String> message) {
		// 消费消息
		T messageObj = JacksonUtil.parseObject(message.getValue(), messageType);
		try {
			log.info("message:{}", message);
			// 消费前处理
			consumeMessageBefore(messageObj);
			// 消费消息
			this.onMessage(messageObj);
			// ack 消息消费完成
			redisMQTemplate.getRedisTemplate().opsForStream().acknowledge(group, message);
		}
		finally {
			// 消费后处理
			consumeMessageAfter(messageObj);
		}
	}

	/**
	 * 处理消息
	 * @param message 消息
	 */
	public abstract void onMessage(T message);

	/**
	 * 解析类上的泛型获得消息类型
	 * @return 消息类型
	 */
	private Class<T> getMessageClass() {
		Type type = TypeUtil.getTypeArgument(getClass(), 0);
		if (type == null) {
			throw new IllegalStateException(String.format("类型(%s) 需要设置消息类型", getClass().getName()));
		}
		return (Class<T>) type;
	}

	private void consumeMessageBefore(AbstractRedisMessage message) {
		List<RedisMessageInterceptor> interceptors = redisMQTemplate.getInterceptors();
		interceptors.forEach(interceptor -> interceptor.consumeMessageBefore(message));
	}

	private void consumeMessageAfter(AbstractRedisMessage message) {
		List<RedisMessageInterceptor> interceptors = redisMQTemplate.getInterceptors();
		for (int i = interceptors.size() - 1; i >= 0; i--) {
			interceptors.get(i).consumeMessageAfter(message);
		}
	}

}
