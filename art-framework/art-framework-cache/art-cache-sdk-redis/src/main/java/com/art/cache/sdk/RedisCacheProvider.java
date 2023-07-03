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

package com.art.cache.sdk;

import com.art.cache.common.DistributedCache;
import com.art.cache.common.DistributedCacheProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/7/3 20:00
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequiredArgsConstructor
public class RedisCacheProvider implements DistributedCacheProvider {

	private final RedisTemplate redisTemplate;

	private Map<String, DistributedCache> cacheManagerMap = new HashMap<>();

	@Override
	public <T> DistributedCache<T> getOrCreate(String cacheName) {
		return cacheManagerMap.computeIfAbsent(cacheName, name -> {
			BoundHashOperations hashOps = redisTemplate.boundHashOps(cacheName);
			return new RedisCache<T>(hashOps);
		});
	}

}
