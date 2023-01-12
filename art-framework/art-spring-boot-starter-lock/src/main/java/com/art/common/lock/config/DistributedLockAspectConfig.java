/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.art.common.lock.config;

import com.art.common.lock.core.aspect.DistributedLockAspect;
import com.art.common.lock.core.lockresolver.impl.DefaultDistributedLockKeyResolver;
import com.art.common.lock.core.lockresolver.impl.ExpressionDistributedLockKeyResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Fxz
 * @version 0.0.1
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
