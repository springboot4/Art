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

package com.art.mq.sdk.support;

import cn.hutool.core.thread.ThreadUtil;
import com.art.mq.sdk.client.HazelcastMQTemplate;
import com.art.mq.sdk.support.broadcast.AbstractHazelcastBroadcastMessageListener;
import com.art.mq.sdk.support.broadcast.HazelcastBroadcastMessage;
import com.art.mq.sdk.support.group.AbstractHazelcastGroupMessageListener;
import com.art.mq.sdk.support.group.HazelcastGroupMessage;
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
@Slf4j
@SuppressWarnings("all")
@RequiredArgsConstructor
public class HazelcastMessageListenerProvider {

	private final HazelcastInstance hazelcastInstance;

	private final HazelcastMQTemplate hazelcastMQTemplate;

	private ExecutorService executorService = ThreadUtil.newExecutor(15);

	public <M extends HazelcastBroadcastMessage> void register(AbstractHazelcastBroadcastMessageListener listener) {
		listener.setMqTemplate(hazelcastMQTemplate);

		String topicName = listener.getTopic();
		ITopic<M> topic = hazelcastInstance.getTopic(topicName);
		topic.addMessageListener(listener);
	}

	public <M extends HazelcastGroupMessage> void register(AbstractHazelcastGroupMessageListener listener) {
		listener.setMqTemplate(hazelcastMQTemplate);

		String groupName = listener.getGroup();
		IQueue<M> queue = hazelcastInstance.getQueue(groupName);
		Thread worker = new Thread(() -> {
			while (true) {
				try {
					M m = queue.take();
					executorService.execute(() -> {
						listener.process(m);
					});
				}
				catch (Throwable e) {
					log.info("{} 线程异常: {}", Thread.currentThread().getName(), e.getLocalizedMessage());
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException interruptedException) {
						log.info("{} 线程异常: {}", Thread.currentThread().getName(),
								interruptedException.getLocalizedMessage());
					}
				}
			}
		}, "HazelcastConsumerThread-" + groupName);

		worker.start();
	}

}
