package com.fxz.common.lock.service.impl;

import com.fxz.common.lock.constant.RedissonLockType;
import com.fxz.common.lock.entity.LockEntity;
import com.fxz.common.lock.service.RedissonService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

/**
 * redisson读锁封装
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/9/4 17:36
 */
@RequiredArgsConstructor
public class RedissonReadLockServiceImpl implements RedissonService {

	private final RedissonClient redissonClient;

	/**
	 * 加锁
	 */
	@Override
	public boolean lock(LockEntity lockEntity) {
		// 获取锁
		RReadWriteLock rLock = redissonClient.getReadWriteLock(lockEntity.getLockName());

		try {
			// 释放锁
			return rLock.readLock().tryLock(lockEntity.getWaitTime(), lockEntity.getLeaseTime(),
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

		if (rLock.readLock().isHeldByCurrentThread()) {
			// 释放锁
			rLock.readLock().unlockAsync();
		}
	}

	/**
	 * 锁类型
	 */
	@Override
	public RedissonLockType lockType() {
		return RedissonLockType.READ;
	}

}
