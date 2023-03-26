
package com.art.common.hazelcast.config;

import com.art.common.hazelcast.core.base.DistributedCacheProvider;
import com.art.common.hazelcast.core.cache.DefaultCacheManager;
import com.art.common.hazelcast.core.cache.HazelcastCacheProvider;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/26 09:44
 */
@AutoConfiguration
public class HazelcastCacheAutoConfiguration {

	@Bean
	DistributedCacheProvider hazelcastCacheProvider(
			@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
		return new HazelcastCacheProvider(hazelcastInstance);
	}

	@Bean
	DefaultCacheManager defaultCacheManager(DistributedCacheProvider distributedCacheProvider) {
		return new DefaultCacheManager(distributedCacheProvider);
	}

}
