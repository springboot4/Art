/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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