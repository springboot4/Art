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

package com.art.demos.controller;

import com.art.common.core.model.Result;
import com.hazelcast.cluster.Member;
import com.hazelcast.collection.IList;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.multimap.MultiMap;
import com.hazelcast.topic.ITopic;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-02-27 18:33
 */
@Tag(name = "Hazelcast测试")
@Slf4j
@RestController
@RequestMapping("/demo/hazelcast")
@RequiredArgsConstructor
public class HazelcastController {

	private final HazelcastInstance hazelcastInstance;

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

		topic.publish(LocalDate.now() + "  fxz test");
		return Result.success();
	}

	@GetMapping(value = "/lock")
	public Result<Void> lock() {
		IMap<String, String> lock = hazelcastInstance.getMap("my-distributed-lock");
		boolean b = false;

		try {
			b = lock.tryLock("lock", 30, TimeUnit.SECONDS);
			TimeUnit.SECONDS.sleep(5);
			log.info(LocalDate.now().toString());
		}
		catch (Throwable t) {
			log.info(t.getMessage());
		}
		finally {
			if (b) {
				lock.unlock("lock");
			}
		}

		return Result.success();
	}

	@GetMapping(value = "/map/save")
	public Result<Void> saveMapData() {
		Map<String, Object> hazelcastMap = hazelcastInstance.getMap("hazelcastMap");
		LocalDate now = LocalDate.now();
		String uuid = "uuid";

		hazelcastMap.put(uuid, now);
		hazelcastMap.putIfAbsent(uuid, "mapVal");
		hazelcastMap.replace(uuid, now, "replaceVal");
		return Result.success();
	}

	@GetMapping(value = "/map/get")
	public Result<String> getMapData() {
		String uuid = "uuid";

		Map<String, String> hazelcastMap = hazelcastInstance.getMap("hazelcastMap");
		return Result.success(hazelcastMap.get(uuid));
	}

	@GetMapping(value = "/map/all")
	public Result<Map<String, String>> readAllDataFromHazelcast() {
		return Result.success(hazelcastInstance.getMap("hazelcastMap"));
	}

	@GetMapping(value = "/multi/data")
	public Result<Object> multiMapData() {
		MultiMap<Object, Object> hazelcastMultiMap = hazelcastInstance.getMultiMap("hazelcastMultiMap");
		hazelcastMultiMap.put("my-key", "value1");
		hazelcastMultiMap.put("my-key", "value2");
		hazelcastMultiMap.put("my-key", "value3");
		return Result.success(hazelcastMultiMap.get("my-key"));
	}

	@GetMapping(value = "/set/data")
	public Result<Set<String>> setData() {
		Set<String> set = hazelcastInstance.getSet("my-distributed-set");
		set.add("item1");
		set.add("item1");
		set.add("item2");
		set.add("item2");
		set.add("item2");
		set.add("item3");

		return Result.success(set);
	}

	@GetMapping(value = "/list/add")
	public Result<Void> saveList(@RequestParam(required = false) String value) {
		IList<Object> clusterList = hazelcastInstance.getList("myList");
		clusterList.add(value);
		return Result.success();
	}

	@GetMapping(value = "/list/showList")
	public Result<IList<Object>> showList() {
		return Result.success(hazelcastInstance.getList("myList"));
	}

	@GetMapping(value = "/clearList")
	public Result<Void> clearList() {
		IList<Object> clusterList = hazelcastInstance.getList("myList");
		clusterList.clear();
		return Result.success();
	}

	@GetMapping(value = "/queue")
	public Result<Void> saveQueue(@RequestParam String value) {
		Queue<String> clusterQueue = hazelcastInstance.getQueue("myQueue");
		clusterQueue.offer(value);
		return Result.success();
	}

	@GetMapping(value = "/showQueue")
	public Result<Queue<String>> showQueue() {
		Queue<String> clusterQueue = hazelcastInstance.getQueue("myQueue");
		return Result.success(clusterQueue);
	}

	@GetMapping(value = "/clearQueue")
	public Result<Void> clearQueue() {
		Queue<String> clusterQueue = hazelcastInstance.getQueue("myQueue");
		clusterQueue.clear();
		return Result.success();
	}

}
