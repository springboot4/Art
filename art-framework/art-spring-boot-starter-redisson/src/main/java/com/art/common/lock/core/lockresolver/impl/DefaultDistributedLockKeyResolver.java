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

package com.art.common.lock.core.lockresolver.impl;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.art.common.lock.core.annotation.DistributedLock;
import com.art.common.lock.core.entity.LockEntity;
import com.art.common.lock.core.lockresolver.DistributedLockResolver;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 默认的key解析器,使用方法名和参数进行md5加密生成key
 *
 * @author fxz
 */
public class DefaultDistributedLockKeyResolver implements DistributedLockResolver {

	/**
	 * 解析一个分布式锁信息
	 * @param distributedLock 分布式锁注解
	 * @param joinPoint AOP 切面
	 * @return 锁信息
	 */
	@Override
	public LockEntity resolver(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
		// 获取锁名称 不指定则根据方法名和参数自动生成
		String lockName = distributedLock.lockName();
		if (StrUtil.isBlank(lockName)) {
			String methodName = joinPoint.getSignature().toString();
			String argsStr = StrUtil.join(StrPool.COMMA, joinPoint.getArgs());
			lockName = SecureUtil.md5(methodName + argsStr);
		}

		return new LockEntity().setLockName(lockName)
			.setLeaseTime(distributedLock.leaseTime())
			.setWaitTime(distributedLock.waitTime())
			.setTimeUnit(distributedLock.timeUnit());
	}

}
