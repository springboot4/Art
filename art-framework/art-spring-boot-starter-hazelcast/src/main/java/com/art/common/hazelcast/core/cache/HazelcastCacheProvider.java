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

package com.art.common.hazelcast.core.cache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/23 15:41
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequiredArgsConstructor
public class HazelcastCacheProvider implements DistributedCacheProvider {

	private final HazelcastInstance hazelcastInstance;

	private Map<String, DistributedCacheManager> cacheManagerMap = new HashMap<>();

	@Override
	public <T> DistributedCacheManager<T> getOrCreate(String cacheName) {
		return cacheManagerMap.computeIfAbsent(cacheName, name -> {
			IMap<String, T> map = hazelcastInstance.getMap(cacheName);
			return new HazelcastCacheManager<T>(map);
		});
	}

}
