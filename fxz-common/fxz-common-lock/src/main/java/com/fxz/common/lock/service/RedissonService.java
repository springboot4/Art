package com.fxz.common.lock.service;

import com.fxz.common.lock.constant.RedissonLockType;
import com.fxz.common.lock.entity.LockEntity;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/4 16:52
 */
public interface RedissonService {

	ThreadLocal<LockEntity> threadLock = new ThreadLocal<>();

	/**
	 * 加锁
	 */
	boolean lock(LockEntity lockEntity);

	/**
	 * 释放锁
	 */
	void unlock(LockEntity lockEntity);

	/**
	 * 锁类型
	 */
	RedissonLockType lockType();

	/**
	 * 获取当前线程锁信息
	 */
	default LockEntity getThreadLock() {
		return threadLock.get();
	}

	/**
	 * 保存当前线程锁信息
	 */
	default void saveThreadLock(LockEntity lockEntity) {
		threadLock.set(lockEntity);
	}

	/**
	 * 清空当前线程的锁信息
	 */
	default void clearThreadLock() {
		// 清空线程信息
		threadLock.remove();
	}

}
