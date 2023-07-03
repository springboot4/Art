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

package com.art.demos.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.art.cache.sdk.HazelcastDefaultCacheClient;
import com.art.cache.sdk.RedisDefaultCacheClient;
import com.art.core.common.model.Result;
import com.art.demos.core.message.DemoGroupMessage;
import com.art.demos.core.message.DemoTopicMessage;
import com.art.hazelcast.sdk.base.*;
import com.art.mq.sdk.client.HazelcastMQTemplate;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-03-25 18:33
 */
@Slf4j
@RestController
@RequestMapping("/demo/hazelcast")
@RequiredArgsConstructor
public class HazelcastController {

	private final HazelcastInstance hazelcastInstance;

	private final HazelcastDefaultCacheClient hazelcastDefaultCacheClient;

	private final DistributedBaseFactory distributedBaseFactory;

	private final HazelcastMQTemplate hazelcastMQTemplate;

	@GetMapping("/send")
	public Result<Void> sendMsg(String msg) {
		DemoTopicMessage topicMessage = new DemoTopicMessage("广播消息,所有的客户端都可以监听到" + msg, LocalDateTime.now());
		hazelcastMQTemplate.send(topicMessage);

		DemoGroupMessage groupMessage = new DemoGroupMessage("消费者组消息,只有一个客户端都可以监听到 " + msg, LocalDateTime.now());
		hazelcastMQTemplate.send(groupMessage);
		return Result.success();
	}

	/**
	 * 分布式锁对象 demo
	 */
	@GetMapping(value = "/lock")
	public Result<Void> lock() {
		boolean flag = false;
		Lock lock = distributedBaseFactory.getLock("demoLock");
		try {
			flag = lock.tryLock(10, TimeUnit.SECONDS);
			TimeUnit.SECONDS.sleep(5);
			log.info("抢锁成功,{}", LocalDateTime.now().toString());
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (flag) {
				lock.unlock();
			}
		}
		return Result.success();
	}

	/**
	 * map demo
	 */
	@GetMapping(value = "/map")
	public Result<Void> map() {
		LocalDate now = LocalDate.now();
		String uuid = "uuid";

		hazelcastDefaultCacheClient.set(uuid, now);
		hazelcastDefaultCacheClient.add(uuid, "mapVal");
		log.info("val:{}", hazelcastDefaultCacheClient.get(uuid));
		hazelcastDefaultCacheClient.replace(uuid, "replaceVal");
		log.info("val:{}", hazelcastDefaultCacheClient.get(uuid));

		now = LocalDate.now();

		DistributedMap<String, String> demoMap = distributedBaseFactory.getMap("demoMap");
		demoMap.put(uuid, now.toString());
		log.info("val:{}", demoMap.get(uuid));
		demoMap.replace(uuid, "replaceValMap");
		log.info("val:{}", demoMap.get(uuid));

		return Result.success();
	}

	/**
	 * set demo
	 */
	@GetMapping(value = "/set")
	public Result<Void> set() {
		DistributedSet<String> demoSet = distributedBaseFactory.getSet("demoSet");
		demoSet.add("item1");
		demoSet.add("item1");
		demoSet.add("item2");
		demoSet.add("item2");
		demoSet.add("item2");
		demoSet.add("item3");

		demoSet.values().forEach(log::info);

		return Result.success();
	}

	/**
	 * queue demo
	 */
	@GetMapping(value = "/queue")
	public Result<Void> queue() {
		DistributedQueue<String> queueDemo = distributedBaseFactory.getQueue("queueDemo");
		queueDemo.offer("a");
		queueDemo.offer("b");
		log.info(queueDemo.poll(1, TimeUnit.SECONDS));
		log.info(queueDemo.poll(1, TimeUnit.SECONDS));
		queueDemo.clear();
		return Result.success();
	}

	/**
	 * countDownLatch demo
	 */
	@SneakyThrows
	@GetMapping(value = "/countDownLatch")
	public Result<Void> countDownLatch() {
		DistributedCountDownLatch countDownLatchDemo = distributedBaseFactory.getCountDownLatch("countDownLatchDemo");
		boolean flag = countDownLatchDemo.trySetCount(5);
		if (!flag) {
			return Result.success();
		}

		for (int i = 0; i < 5; i++) {
			ThreadUtil.execute(() -> {
				try {
					TimeUnit.SECONDS.sleep(10);
					log.info("线程:{}执行完毕", Thread.currentThread().getId());
					countDownLatchDemo.countDown();
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			});
		}

		countDownLatchDemo.await(20, TimeUnit.SECONDS);
		log.info("方法执行完毕");

		return Result.success();
	}

	@GetMapping(value = "/topic")
	public Result<Void> topic() {
		ITopic<Object> topic = hazelcastInstance.getTopic("my-distributed-topic");

		topic.addMessageListener((message) -> {
			Object messageObject = message.getMessageObject();
			long publishTime = message.getPublishTime();
			Member publishingMember = message.getPublishingMember();
			Object source = message.getSource();

			log.info("messageObject:{}", messageObject);
			log.info("publishTime:{}", publishTime);
			log.info("publishingMember:{}", publishingMember);
			log.info("source:{}", source);
		});

		topic.publish(LocalDate.now() + " fxz test");
		return Result.success();
	}

}
