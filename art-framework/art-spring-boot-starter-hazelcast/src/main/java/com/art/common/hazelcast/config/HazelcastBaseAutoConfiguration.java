package com.art.common.hazelcast.config;

import com.art.common.hazelcast.core.base.DistributedCountDownLatchFactory;
import com.art.common.hazelcast.core.base.DistributedLockFactory;
import com.art.common.hazelcast.core.base.DistributedMapFactory;
import com.art.common.hazelcast.core.base.DistributedQueueFactory;
import com.art.common.hazelcast.core.base.DistributedSetFactory;
import com.art.common.hazelcast.core.support.HazelcastCountDownLatchFactory;
import com.art.common.hazelcast.core.support.HazelcastLockFactory;
import com.art.common.hazelcast.core.support.HazelcastMapFactory;
import com.art.common.hazelcast.core.support.HazelcastProperties;
import com.art.common.hazelcast.core.support.HazelcastQueueFactory;
import com.art.common.hazelcast.core.support.HazelcastSetFactory;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/26 09:44
 */
@EnableConfigurationProperties(HazelcastProperties.class)
@AutoConfiguration
public class HazelcastBaseAutoConfiguration {

	@Bean
	DistributedCountDownLatchFactory distributedCountDownLatchFactory(
			@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
		return new HazelcastCountDownLatchFactory(hazelcastInstance);
	}

	@Bean
	DistributedLockFactory distributedLockFactory(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
		return new HazelcastLockFactory(hazelcastInstance);
	}

	@Bean
	DistributedMapFactory distributedMapFactory(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
		return new HazelcastMapFactory(hazelcastInstance);
	}

	@Bean
	DistributedQueueFactory distributedQueueFactory(
			@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
		return new HazelcastQueueFactory(hazelcastInstance);
	}

	@Bean
	DistributedSetFactory distributedSetFactory(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
		return new HazelcastSetFactory(hazelcastInstance);
	}

}