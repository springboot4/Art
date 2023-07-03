
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

package com.art.cache.starter;

import com.art.cache.common.DistributedCacheProvider;
import com.art.cache.sdk.RedisCacheProvider;
import com.art.cache.sdk.RedisDefaultCacheClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/26 09:44
 */
@AutoConfiguration
public class RedisCacheAutoConfiguration {

	@Bean
	RedisCacheProvider redisCacheProvider(RedisTemplate redisTemplate) {
		return new RedisCacheProvider(redisTemplate);
	}

	@Bean
	RedisDefaultCacheClient redisDefaultCacheClient(RedisCacheProvider redisCacheProvider) {
		return new RedisDefaultCacheClient(redisCacheProvider);
	}

}
