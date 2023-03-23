package com.art.common.hazelcast.config;

import com.art.common.hazelcast.core.HazelcastInstanceFactory;
import com.art.common.hazelcast.core.HazelcastProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * HazelcastAutoConfiguration
 * @author fxz
 */
@EnableConfigurationProperties(HazelcastProperties.class)
@AutoConfiguration
public class HazelcastAutoConfiguration {

	@Bean
	HazelcastInstanceFactory hazelcastInstance(HazelcastProperties hazelcastProperties) {
		return new HazelcastInstanceFactory(hazelcastProperties);
	}

}