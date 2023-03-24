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

import cn.hutool.core.thread.ThreadUtil;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/24 20:24
 */
@SuppressWarnings("all")
@Slf4j
@RequiredArgsConstructor
public class HazelcastMessageListenerProvider {

	private final HazelcastInstance hazelcastInstance;

	private final ExecutorService executorService = ThreadUtil.newExecutor(15);

	public <M extends AbstractTopicMessage> void register(AbstractTopicMessageListener<M> listener) {
		String topicName = listener.getTopic();
		ITopic<M> topic = hazelcastInstance.getTopic(topicName);
		topic.addMessageListener(listener);
	}

	public <M extends AbstractGroupMessage> void register(AbstractGroupMessageListener<M> listener) {
		String groupName = listener.getGroup();
		IQueue<M> queue = hazelcastInstance.getQueue(groupName);
		Thread worker = new Thread(() -> {
			while (true) {
				try {
					M m = queue.take();
					executorService.execute(() -> {
						listener.onMessage(m);
					});
				}
				catch (InterruptedException e) {
					log.info("消费失败:{}", groupName);
				}
			}
		}, "HazelcastConsumerThread-" + groupName);

		worker.start();
	}

}
