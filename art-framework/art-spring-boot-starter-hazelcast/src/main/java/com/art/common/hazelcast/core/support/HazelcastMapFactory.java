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

import com.art.common.hazelcast.core.base.DistributedMap;
import com.art.common.hazelcast.core.base.DistributedMapFactory;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/23 17:54
 */
@RequiredArgsConstructor
public class HazelcastMapFactory implements DistributedMapFactory {

	private final HazelcastInstance instance;

	/**
	 * 获取分布式集合
	 * @param name 分布式集合的名称
	 * @return 分布式集合
	 */
	@Override
	public <K, V> DistributedMap<K, V> getMap(String name) {
		return new HazelcastMap<>(instance.getMap(name));
	}

}
