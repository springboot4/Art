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
import lombok.extern.slf4j.Slf4j;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/24 19:52
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("all")
public class HazelcastMQTemplate {

	private final HazelcastInstance hazelcastInstance;

	/**
	 * 发送分组消息
	 * @param message 消费者组消息
	 * @param <T> 消费者组消息
	 */
	public <T extends AbstractGroupMessage> void send(T message) {
		String groupName = message.getGroup();
		IQueue<T> queue = hazelcastInstance.getQueue(groupName);
		try {
			queue.put(message);
		}
		catch (InterruptedException e) {
			log.info("消息发送失败,queue:{}", queue);
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

}
