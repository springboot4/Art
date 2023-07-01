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

package com.art.common.Idempotent.redis;

import cn.hutool.core.text.StrPool;
import com.art.common.Idempotent.constant.IdempotentConstant;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 幂等服务操作redis
 *
 * @author fxz
 */
@SuppressWarnings("all")
@AllArgsConstructor
public class IdempotentRedisService {

	private final StringRedisTemplate redisTemplate;

	public Boolean setIfAbsent(String key, long timeout, TimeUnit timeUnit) {
		return redisTemplate.opsForValue()
			.setIfAbsent(String.format(IdempotentConstant.REDIS_FORMAT, key), StrPool.EMPTY_JSON, timeout, timeUnit);
	}

	public Boolean removeKey(String key) {
		return redisTemplate.delete(String.format(IdempotentConstant.REDIS_FORMAT, key));
	}

}
