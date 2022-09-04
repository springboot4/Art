package com.fxz.common.lock.service.impl;

import com.fxz.common.lock.constant.RedissonLockType;
import com.fxz.common.lock.entity.LockEntity;
import com.fxz.common.lock.service.RedissonService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * redisson公平锁封装
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/9/4 17:11
 */
@RequiredArgsConstructor
public class RedissonFairLockServiceImpl implements RedissonService {

	private final RedissonClient redissonClient;

	/**
	 * 加锁
	 */
	@Override
	public boolean lock(LockEntity lockEntity) {
		// 获取锁
		RLock rLock = redissonClient.getFairLock(lockEntity.getLockName());

		try {
			// 加锁
			return rLock.tryLock(lockEntity.getWaitTime(), lockEntity.getLeaseTime(), lockEntity.getTimeUnit());
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
		RLock rLock = redissonClient.getFairLock(lockEntity.getLockName());

		if (rLock.isHeldByCurrentThread()) {
			// 解锁
			rLock.unlockAsync();
		}
	}

	/**
	 * 锁类型
	 */
	@Override
	public RedissonLockType lockType() {
		return RedissonLockType.FAIR;
	}

}
