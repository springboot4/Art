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

package com.art.demos.core.distributed.lock.client;

import com.art.demos.core.distributed.lock.redis.DistributedRedisLock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/3 15:33
 */
@Component
@RequiredArgsConstructor
public class DistributedLockClient {

	private final StringRedisTemplate redisTemplate;

	/**
	 * DistributedLockClient为单例 uuid在每个服务下唯一
	 */
	private final String uuid = UUID.randomUUID().toString();

	public DistributedRedisLock getRedisLock(String lockName) {
		return new DistributedRedisLock(redisTemplate, lockName, uuid);
	}

}
