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

package com.fxz.common.lock.service;

import com.fxz.common.lock.constant.RedissonLockType;
import com.fxz.common.lock.entity.LockEntity;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/4 16:52
 */
public interface RedissonService {

	ThreadLocal<LockEntity> THREAD_LOCK = new ThreadLocal<>();

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
		return THREAD_LOCK.get();
	}

	/**
	 * 保存当前线程锁信息
	 */
	default void saveThreadLock(LockEntity lockEntity) {
		THREAD_LOCK.set(lockEntity);
	}

	/**
	 * 清空当前线程的锁信息
	 */
	default void clearThreadLock() {
		// 清空线程信息
		THREAD_LOCK.remove();
	}

}
