package com.fxz.common.tenant.cache;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fxz.common.mq.redis.core.RedisMQTemplate;
import com.fxz.common.redis.cache.properties.CacheRedisCaffeineProperties;
import com.fxz.common.redis.cache.support.RedisCaffeineCacheManager;
import com.fxz.common.tenant.context.TenantContextHolder;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * redis key中拼接租户id
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 12:18
 */
public class TenantRedisCacheManager extends RedisCaffeineCacheManager {

	public TenantRedisCacheManager(CacheRedisCaffeineProperties cacheRedisCaffeineProperties,
			RedisTemplate redisTemplate, RedisMQTemplate redisMQTemplate) {
		super(cacheRedisCaffeineProperties, redisTemplate, redisMQTemplate);
	}

	@Override
	public Cache getCache(String name) {
		if (Objects.nonNull(TenantContextHolder.getTenantId())) {
			name = TenantContextHolder.getTenantId() + StringPool.COLON + name;
		}

		return super.getCache(name);
	}

}
