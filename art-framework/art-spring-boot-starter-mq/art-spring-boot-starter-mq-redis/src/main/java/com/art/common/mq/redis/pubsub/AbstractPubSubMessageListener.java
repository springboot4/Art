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

package com.art.common.mq.redis.pubsub;

import cn.hutool.core.util.TypeUtil;
import com.art.common.jackson.util.JacksonUtil;
import com.art.common.mq.redis.core.RedisMQTemplate;
import com.art.common.mq.redis.interceptor.RedisMessageInterceptor;
import com.art.common.mq.redis.message.AbstractRedisMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/27 21:48
 */
@SuppressWarnings("all")
@Setter
@Getter
@Slf4j
public abstract class AbstractPubSubMessageListener<T extends AbstractPubSubMessage> implements MessageListener {

	/**
	 * 消息类型
	 */
	private final Class<T> messageType;

	/**
	 * topic
	 */
	private final String topic;

	/**
	 * RedisMQTemplate
	 */
	private RedisMQTemplate redisMQTemplate;

	@SneakyThrows
	protected AbstractPubSubMessageListener() {
		this.messageType = getMessageClass();
		this.topic = messageType.newInstance().getTopic();
	}

	@Override
	public final void onMessage(Message message, byte[] bytes) {
		T messageObj = JacksonUtil.parseObject(message.getBody(), messageType);
		try {
			consumeMessageBefore(messageObj);
			// 消费消息
			this.onMessage(messageObj);
		}
		finally {
			consumeMessageAfter(messageObj);
		}
	}

	/**
	 * 处理消息
	 */
	public abstract void onMessage(T message);

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
