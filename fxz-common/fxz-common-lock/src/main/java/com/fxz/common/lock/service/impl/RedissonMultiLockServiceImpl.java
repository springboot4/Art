package com.fxz.common.lock.service.impl;

import com.fxz.common.lock.constant.RedissonLockType;
import com.fxz.common.lock.entity.LockEntity;
import com.fxz.common.lock.service.RedissonService;
import lombok.RequiredArgsConstructor;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * redisson联锁封装
 *
 * @author Fxz
 * @version 1.0
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
