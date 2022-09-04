package com.fxz.common.lock.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.fxz.common.lock.constant.RedissonLockType;
import com.fxz.common.lock.service.RedissonService;

import java.util.Map;

/**
 * redisson锁工厂
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/9/4 16:37
 */
public class RedissonLockServiceFactory {

	/**
	 * 根据类型获取不同的锁实现
	 * @param redissonLockType 锁类型
	 */
	public static RedissonService getLock(RedissonLockType redissonLockType) {
		Map<String, RedissonService> serviceMap = SpringUtil.getBeansOfType(RedissonService.class);
		return serviceMap.values().stream().filter(s -> redissonLockType.equals(s.lockType())).findFirst().orElse(null);
	}

}
