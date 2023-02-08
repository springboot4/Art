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

package com.art.common.tenant.cache;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.art.common.mq.redis.core.RedisMQTemplate;
import com.art.common.redis.core.cache.properties.CacheRedisCaffeineProperties;
import com.art.common.redis.core.cache.support.RedisCaffeineCacheManager;
import com.art.common.tenant.context.TenantContextHolder;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * redis key中拼接租户id
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/1 12:18
 */
public class TenantRedisCaffeineCacheManager extends RedisCaffeineCacheManager {

	public TenantRedisCaffeineCacheManager(CacheRedisCaffeineProperties cacheRedisCaffeineProperties,
			RedisTemplate redisTemplate, RedisMQTemplate redisMQTemplate) {
		super(cacheRedisCaffeineProperties, redisTemplate, redisMQTemplate);
	}

	@Override
	public Cache getCache(String name) {
		if (Objects.nonNull(TenantContextHolder.getTenantId())) {
			name = TenantContextHolder.getTenantId() + StringPool.COLON + name;
		}

		return super.getCache(name);
	}

}
