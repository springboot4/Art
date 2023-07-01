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

package com.art.common.lock.core.aspect;

import cn.hutool.extra.spring.SpringUtil;
import com.art.common.lock.core.annotation.DistributedLock;
import com.art.common.lock.core.entity.LockEntity;
import com.art.common.lock.core.factory.RedissonLockServiceFactory;
import com.art.common.lock.core.lockresolver.DistributedLockResolver;
import com.art.common.lock.core.service.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/4 23:16
 */
@Slf4j
@Aspect
public class DistributedLockAspect implements Ordered {

	/**
	 * 对带有DistributedLock注解的进行幂等处理，使用环绕通知
	 */
	@Around(value = "@annotation(distributedLock)")
	public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
		// 获得 key 解析器
		DistributedLockResolver keyResolver = SpringUtil.getBean(distributedLock.lockResolver());
		Assert.notNull(keyResolver, "找不到对应的 DistributedLockResolver");

		// 解析锁信息
		LockEntity lockEntity = keyResolver.resolver(joinPoint, distributedLock);

		// 根据锁类型 获取redissonService
		RedissonService redissonService = RedissonLockServiceFactory.getLock(distributedLock.lockType());

		// 如果当前线程持有当前锁，则重入
		if (redissonService.getThreadLock() != null && !lockEntity.equals(redissonService.getThreadLock())) {
			return joinPoint.proceed();
		}

		// 保存线程锁信息
		redissonService.saveThreadLock(lockEntity);

		// 加锁
		boolean success = redissonService.lock(lockEntity);
		try {
			// 锁定失败，抛出异常
			if (!success) {
				throw new RuntimeException("加锁失败！");
			}
			else {
				return joinPoint.proceed();
			}
		}
		finally {
			if (success) {
				// 释放锁
				redissonService.unlock(lockEntity);
				// 清空线程锁信息
				redissonService.clearThreadLock();
			}
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
