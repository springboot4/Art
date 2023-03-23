package com.art.common.hazelcast.config;

import com.art.common.hazelcast.core.base.DistributedCacheProvider;
import com.art.common.hazelcast.core.base.DistributedCountDownLatchFactory;
import com.art.common.hazelcast.core.base.DistributedLockFactory;
import com.art.common.hazelcast.core.base.DistributedMapFactory;
import com.art.common.hazelcast.core.base.DistributedQueueFactory;
import com.art.common.hazelcast.core.base.DistributedSetFactory;
import com.art.common.hazelcast.core.cache.DefaultCacheManager;
import com.art.common.hazelcast.core.cache.HazelcastCacheProvider;
import com.art.common.hazelcast.core.support.HazelcastCountDownLatchFactory;
import com.art.common.hazelcast.core.support.HazelcastInstanceFactory;
import com.art.common.hazelcast.core.support.HazelcastLockFactory;
import com.art.common.hazelcast.core.support.HazelcastMapFactory;
import com.art.common.hazelcast.core.support.HazelcastProperties;
import com.art.common.hazelcast.core.support.HazelcastQueueFactory;
import com.art.common.hazelcast.core.support.HazelcastSetFactory;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * HazelcastAutoConfiguration
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/3/23 16:16
 */
@EnableConfigurationProperties(HazelcastProperties.class)
@AutoConfiguration
public class HazelcastAutoConfiguration {

	@Bean
	HazelcastInstanceFactory hazelcastInstance(HazelcastProperties hazelcastProperties) {
		return new HazelcastInstanceFactory(hazelcastProperties);
	}

	@Bean
	DistributedCacheProvider hazelcastCacheProvider(HazelcastInstance hazelcastInstance) {
		return new HazelcastCacheProvider(hazelcastInstance);
	}

	@Bean
	DefaultCacheManager defaultCacheManager(DistributedCacheProvider distributedCacheProvider) {
		return new DefaultCacheManager(distributedCacheProvider);
	}

	@Bean
	DistributedCountDownLatchFactory distributedCountDownLatchFactory(HazelcastInstance hazelcastInstance) {
		return new HazelcastCountDownLatchFactory(hazelcastInstance);
	}

	@Bean
	DistributedLockFactory distributedLockFactory(HazelcastInstance hazelcastInstance) {
		return new HazelcastLockFactory(hazelcastInstance);
	}

	@Bean
	DistributedMapFactory distributedMapFactory(HazelcastInstance hazelcastInstance) {
		return new HazelcastMapFactory(hazelcastInstance);
	}

	@Bean
	DistributedQueueFactory distributedQueueFactory(HazelcastInstance hazelcastInstance) {
		return new HazelcastQueueFactory(hazelcastInstance);
	}

	@Bean
	DistributedSetFactory distributedSetFactory(HazelcastInstance hazelcastInstance) {
		return new HazelcastSetFactory(hazelcastInstance);
	}

}