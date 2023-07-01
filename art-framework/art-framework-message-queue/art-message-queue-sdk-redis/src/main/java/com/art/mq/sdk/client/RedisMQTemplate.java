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

package com.art.mq.sdk.client;

import com.art.common.jackson.util.JacksonUtil;
import com.art.mq.common.client.MessageQueueTemplate;
import com.art.mq.common.message.AbstractBroadcastMessage;
import com.art.mq.common.message.AbstractGroupMessage;
import com.art.mq.sdk.interceptor.RedisMessageInterceptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis消息队列操作类
 *
 * @author fxz
 */
@Getter
@SuppressWarnings("all")
@AllArgsConstructor
public class RedisMQTemplate extends MessageQueueTemplate<RedisMessageInterceptor> {

	private final RedisTemplate<String, ?> redisTemplate;

	@Override
	protected void doSend(AbstractGroupMessage message) {
		ObjectRecord<String, String> record = StreamRecords.newRecord()
			.ofObject(JacksonUtil.toJsonString(message))
			.withStreamKey(message.getGroup());

		// 发送分组消息
		redisTemplate.opsForStream().add(record);
	}

	@Override
	protected void doSend(AbstractBroadcastMessage message) {
		// 发送广播消息
		redisTemplate.convertAndSend(message.getTopic(), JacksonUtil.toJsonString(message));
	}

}
