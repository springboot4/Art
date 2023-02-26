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

package com.art.common.redis.core.mq.client;

import com.art.common.jackson.util.JacksonUtil;
import com.art.common.redis.core.mq.interceptor.RedisMessageInterceptor;
import com.art.common.redis.core.mq.message.AbstractRedisMessage;
import com.art.common.redis.core.mq.pubsub.AbstractPubSubMessage;
import com.art.common.redis.core.mq.stream.AbstractStreamMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * redis消息队列操作类
 *
 * @author fxz
 */
@Getter
@SuppressWarnings("all")
@AllArgsConstructor
public class RedisMQTemplate {

	private final RedisTemplate<String, ?> redisTemplate;

	/**
	 * 拦截器数组
	 */
	private final List<RedisMessageInterceptor> interceptors = new ArrayList<>();

	/**
	 * 基于Stream
	 * @param message 消息
	 * @return 消息记录的编号对象
	 */
	public <T extends AbstractStreamMessage> RecordId send(T message) {
		try {
			// 发送消息前拦截器处理
			sendMessageBefore(message);
			// 构建 ObjectRecord
			ObjectRecord<String, String> record = StreamRecords.newRecord()
				.ofObject(JacksonUtil.toJsonString(message))
				.withStreamKey(message.getStreamKey());
			// 发送消息
			return redisTemplate.opsForStream().add(record);
		}
		finally {
			// 消息发送后拦截器处理
			sendMessageAfter(message);
		}
	}

	/**
	 * 基于发布订阅
	 */
	public <T extends AbstractPubSubMessage> void send(T message) {
		try {
			sendMessageBefore(message);
			redisTemplate.convertAndSend(message.getTopic(), JacksonUtil.toJsonString(message));
		}
		finally {
			sendMessageAfter(message);
		}
	}

	/**
	 * 添加拦截器
	 */
	public void addInterceptor(RedisMessageInterceptor interceptor) {
		interceptors.add(interceptor);
	}

	private void sendMessageBefore(AbstractRedisMessage message) {
		interceptors.forEach(interceptor -> interceptor.sendMessageBefore(message));
	}

	private void sendMessageAfter(AbstractRedisMessage message) {
		for (int i = interceptors.size() - 1; i >= 0; i--) {
			interceptors.get(i).sendMessageAfter(message);
		}
	}

}
