package com.fxz.common.lock.lockresolver;

import com.fxz.common.lock.annotation.DistributedLock;
import com.fxz.common.lock.entity.LockEntity;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author fxz
 */
public interface DistributedLockResolver {

	/**
	 * 解析一个分布式锁信息
	 * @param distributedLock 分布式锁注解
	 * @param joinPoint AOP 切面
	 * @return 锁信息
	 */
	LockEntity resolver(ProceedingJoinPoint joinPoint, DistributedLock distributedLock);

}
