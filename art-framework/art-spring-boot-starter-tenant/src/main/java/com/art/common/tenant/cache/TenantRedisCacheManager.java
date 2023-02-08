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

package com.art.common.tenant.cache;

import com.art.common.tenant.context.TenantContextHolder;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Objects;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/2/8 11:44
 */
public class TenantRedisCacheManager extends RedisCacheManager {

	public TenantRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
		super(cacheWriter, defaultCacheConfiguration);
	}

	@Override
	public Cache getCache(String name) {
		if (Objects.nonNull(TenantContextHolder.getTenantId())) {
			name = TenantContextHolder.getTenantId() + StringPool.COLON + name;
		}

		return super.getCache(name);
	}

}
