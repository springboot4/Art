package com.fxz.common.Idempotent.redis;

import cn.hutool.core.text.StrPool;
import com.fxz.common.Idempotent.constant.IdempotentConstant;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 幂等服务操作redis
 *
 * @author fxz
 */
@SuppressWarnings("all")
@AllArgsConstructor
public class IdempotentRedisService {

	private final StringRedisTemplate redisTemplate;

	public Boolean setIfAbsent(String key, long timeout, TimeUnit timeUnit) {
		return redisTemplate.opsForValue().setIfAbsent(String.format(IdempotentConstant.REDIS_FORMAT, key),
				StrPool.EMPTY_JSON, timeout, timeUnit);
	}

	public Boolean removeKey(String key) {
		return redisTemplate.delete(String.format(IdempotentConstant.REDIS_FORMAT, key));
	}

}
