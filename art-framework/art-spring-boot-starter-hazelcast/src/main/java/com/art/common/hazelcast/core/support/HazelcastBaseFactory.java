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

package com.art.common.hazelcast.core.support;

import com.art.common.hazelcast.core.base.DistributedBaseFactory;
import com.art.common.hazelcast.core.base.DistributedCountDownLatch;
import com.art.common.hazelcast.core.base.DistributedMap;
import com.art.common.hazelcast.core.base.DistributedQueue;
import com.art.common.hazelcast.core.base.DistributedSet;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Lock;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/27 09:11
 */
@RequiredArgsConstructor
public class HazelcastBaseFactory implements DistributedBaseFactory {

	private final HazelcastInstance instance;

	/**
	 * 获取分布式集合
	 * @param name 分布式集合的名称
	 * @return 分布式集合
	 */
	@Override
	public DistributedCountDownLatch getCountDownLatch(String name) {
		return new HazelcastCountDownLatch(instance.getCPSubsystem().getCountDownLatch(name));
	}

	/**
	 * 获取分布式集合
	 * @param name 分布式集合的名称
	 * @return 分布式集合
	 */
	@Override
	public Lock getLock(String name) {
		return instance.getCPSubsystem().getLock(name);
	}

	/**
	 * 获取分布式集合
	 * @param name 分布式集合的名称
	 * @return 分布式集合
	 */
	@Override
	public <K, V> DistributedMap<K, V> getMap(String name) {
		return new HazelcastMap<>(instance.getMap(name));
	}

	/**
	 * 获取分布式集合
	 * @param name 分布式集合的名称
	 * @return 分布式集合
	 */
	@Override
	public <T> DistributedQueue<T> getQueue(String name) {
		return new HazelcastQueue<>(instance.getQueue(name));
	}

	/**
	 * 获取分布式集合
	 * @param name 分布式集合的名称
	 * @param <T> 泛型
	 * @return 分布式集合
	 */
	@Override
	public <T> DistributedSet<T> getSet(String name) {
		return new HazelcastSet<>(instance.getSet(name));
	}

}
