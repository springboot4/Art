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

package com.art.common.hazelcast.core.mq;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/24 19:52
 */
@RequiredArgsConstructor
@SuppressWarnings("all")
public class HazelcastMQTemplate {

	private final HazelcastInstance hazelcastInstance;

	private final List<HazelcastMessageInterceptor> interceptors;

	/**
	 * 发送分组消息
	 * @param message 消费者组消息
	 * @param <T> 消费者组消息
	 */
	public <T extends AbstractGroupMessage> void send(T message) {
		String groupName = message.getGroup();
		IQueue<T> queue = hazelcastInstance.getQueue(groupName);
		try {
			sendMessageBefore(message);
			queue.put(message);
			sendMessageBefore(message);
		}
		catch (InterruptedException e) {
		}
	}

	/**
	 * 发送广播消息
	 * @param message 广播消息
	 * @param <T> 广播消息
	 */
	public <T extends AbstractTopicMessage> void send(T message) {
		String topicName = message.getTopic();
		ITopic<T> topic = hazelcastInstance.getTopic(topicName);
		topic.publish(message);
	}

	private void sendMessageBefore(AbstractMessage message) {
		interceptors.forEach(interceptor -> interceptor.sendMessageBefore(message));
	}

	private void sendMessageAfter(AbstractMessage message) {
		for (int i = interceptors.size() - 1; i >= 0; i--) {
			interceptors.get(i).sendMessageAfter(message);
		}
	}

}
