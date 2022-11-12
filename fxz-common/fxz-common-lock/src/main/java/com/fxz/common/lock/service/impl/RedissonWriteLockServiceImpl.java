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

package com.fxz.common.lock.service.impl;

import com.fxz.common.lock.constant.RedissonLockType;
import com.fxz.common.lock.entity.LockEntity;
import com.fxz.common.lock.service.RedissonService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/4 17:47
 */
@RequiredArgsConstructor
public class RedissonWriteLockServiceImpl implements RedissonService {

	private final RedissonClient redissonClient;

	/**
	 * 加锁
	 */
	@Override
	public boolean lock(LockEntity lockEntity) {
		// 获取锁
		RReadWriteLock rLock = redissonClient.getReadWriteLock(lockEntity.getLockName());

		try {
			// 加锁
			return rLock.writeLock().tryLock(lockEntity.getWaitTime(), lockEntity.getLeaseTime(),
					lockEntity.getTimeUnit());
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
		RReadWriteLock rLock = redissonClient.getReadWriteLock(lockEntity.getLockName());

		if (rLock.writeLock().isHeldByCurrentThread()) {
			// 释放锁
			rLock.writeLock().unlockAsync();
		}
	}

	/**
	 * 锁类型
	 */
	@Override
	public RedissonLockType lockType() {
		return RedissonLockType.WRITE;
	}

}
