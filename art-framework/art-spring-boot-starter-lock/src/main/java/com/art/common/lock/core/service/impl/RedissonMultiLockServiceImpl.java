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

package com.art.common.lock.core.service.impl;

import com.art.common.lock.core.constant.RedissonLockType;
import com.art.common.lock.core.entity.LockEntity;
import com.art.common.lock.core.service.RedissonService;
import lombok.RequiredArgsConstructor;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * redisson联锁封装
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/4 17:26
 */
@RequiredArgsConstructor
public class RedissonMultiLockServiceImpl implements RedissonService {

	private final RedissonClient redissonClient;

	/**
	 * 加锁
	 */
	@Override
	public boolean lock(LockEntity lockEntity) {
		// 获取锁
		RLock[] locks = lockEntity.getKeyList().stream().map(redissonClient::getLock).toArray(RLock[]::new);
		RedissonMultiLock lock = new RedissonMultiLock(locks);

		try {
			// 加锁
			return lock.tryLock(lockEntity.getWaitTime(), lockEntity.getLeaseTime(), lockEntity.getTimeUnit());
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * 释放锁
	 */
	@Override
	public void unlock(LockEntity lockEntity) {
		// 获取锁
		RLock[] locks = lockEntity.getKeyList().stream().map(redissonClient::getLock).toArray(RLock[]::new);
		RedissonMultiLock lock = new RedissonMultiLock(locks);

		// 解锁
		lock.unlock();
	}

	/**
	 * 锁类型
	 */
	@Override
	public RedissonLockType lockType() {
		return RedissonLockType.MULTI;
	}

}
