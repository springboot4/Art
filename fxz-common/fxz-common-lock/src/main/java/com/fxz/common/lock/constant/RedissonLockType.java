package com.fxz.common.lock.constant;

import lombok.NoArgsConstructor;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/4 17:55
 */
@NoArgsConstructor
public enum RedissonLockType {

	/**
	 * 可重入锁
	 */
	REENTRANT,

	/**
	 * 公平锁
	 */
	FAIR,

	/**
	 * 联锁
	 */
	MULTI,

	/**
	 * 红锁
	 */
	RED,

	/**
	 * 读锁
	 */
	READ,

	/**
	 * 写锁
	 */
	WRITE;

}
