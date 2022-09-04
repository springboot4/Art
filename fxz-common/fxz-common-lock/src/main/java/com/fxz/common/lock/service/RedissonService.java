package com.fxz.common.lock.service;

import com.fxz.common.lock.constant.RedissonLockType;
import com.fxz.common.lock.entity.LockEntity;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/4 16:52
 */
public interface RedissonService {

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

}
