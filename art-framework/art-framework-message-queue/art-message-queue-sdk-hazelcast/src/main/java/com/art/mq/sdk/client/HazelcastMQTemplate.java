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

import com.art.mq.common.client.MessageQueueTemplate;
import com.art.mq.common.message.AbstractBroadcastMessage;
import com.art.mq.common.message.AbstractGroupMessage;
import com.art.mq.sdk.interceptor.HazelcastMessageInterceptor;
import com.art.mq.sdk.support.broadcast.HazelcastBroadcastMessage;
import com.art.mq.sdk.support.group.HazelcastGroupMessage;
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
public class HazelcastMQTemplate extends MessageQueueTemplate<HazelcastMessageInterceptor> {

	private final HazelcastInstance hazelcastInstance;

	private final List<HazelcastMessageInterceptor> interceptors;

	/**
	 * 发送分组消息
	 * @param message 消费者组消息
	 * @param <T> 消费者组消息
	 */
	protected <T extends AbstractGroupMessage> void doSend(T message) {
		String groupName = message.getGroup();
		IQueue<T> queue = hazelcastInstance.getQueue(groupName);
		try {
			queue.put(message);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
	}

	/**
	 * 发送广播消息
	 * @param message 广播消息
	 * @param <T> 广播消息
	 */
	protected <T extends AbstractBroadcastMessage> void doSend(T message) {
		String topicName = message.getTopic();
		ITopic<T> topic = hazelcastInstance.getTopic(topicName);
		topic.publish(message);
	}

}
