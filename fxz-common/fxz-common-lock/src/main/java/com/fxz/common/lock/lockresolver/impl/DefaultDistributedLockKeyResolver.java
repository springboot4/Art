package com.fxz.common.lock.lockresolver.impl;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.fxz.common.lock.annotation.DistributedLock;
import com.fxz.common.lock.entity.LockEntity;
import com.fxz.common.lock.lockresolver.DistributedLockResolver;
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

		return new LockEntity().setLockName(lockName).setLeaseTime(distributedLock.leaseTime())
				.setWaitTime(distributedLock.waitTime()).setTimeUnit(distributedLock.timeUnit());
	}

}