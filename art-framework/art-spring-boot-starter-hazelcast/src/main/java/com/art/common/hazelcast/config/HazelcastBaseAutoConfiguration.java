package com.art.common.hazelcast.config;

import com.art.common.hazelcast.core.base.DistributedBaseFactory;
import com.art.common.hazelcast.core.support.HazelcastBaseFactory;
import com.art.common.hazelcast.core.support.HazelcastProperties;
import com.hazelcast.core.HazelcastInstance;
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
	DistributedBaseFactory distributedBaseFactory(HazelcastInstance instance) {
		return new HazelcastBaseFactory(instance);
	}

}