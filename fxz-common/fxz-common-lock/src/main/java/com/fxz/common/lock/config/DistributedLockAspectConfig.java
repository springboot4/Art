package com.fxz.common.lock.config;

import com.fxz.common.lock.aspect.DistributedLockAspect;
import com.fxz.common.lock.lockresolver.impl.DefaultDistributedLockKeyResolver;
import com.fxz.common.lock.lockresolver.impl.ExpressionDistributedLockKeyResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/5 00:37
 */
@AutoConfiguration
public class DistributedLockAspectConfig {

	@Bean
	public DefaultDistributedLockKeyResolver defaultDistributedLockKeyResolver() {
		return new DefaultDistributedLockKeyResolver();
	}

	@Bean
	public ExpressionDistributedLockKeyResolver expressionDistributedLockKeyResolver() {
		return new ExpressionDistributedLockKeyResolver();
	}

	@Bean
	public DistributedLockAspect distributedLockAspect() {
		return new DistributedLockAspect();
	}

}
