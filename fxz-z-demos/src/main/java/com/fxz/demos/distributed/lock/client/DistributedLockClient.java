package com.fxz.demos.distributed.lock.client;

import com.fxz.demos.distributed.lock.redis.DistributedRedisLock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/3 15:33
 */
@Component
@RequiredArgsConstructor
public class DistributedLockClient {

	private final StringRedisTemplate redisTemplate;

	/**
	 * DistributedLockClient为单例 uuid在每个服务下唯一
	 */
	private final String uuid = UUID.randomUUID().toString();

	public DistributedRedisLock getRedisLock(String lockName) {
		return new DistributedRedisLock(redisTemplate, lockName, uuid);
	}

}
