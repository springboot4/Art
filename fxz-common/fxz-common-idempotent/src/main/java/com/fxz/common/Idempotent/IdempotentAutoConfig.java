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

package com.fxz.common.Idempotent;

import com.fxz.common.Idempotent.aspect.IdempotentAspect;
import com.fxz.common.Idempotent.keyresolver.impl.DefaultIdempotentKeyResolver;
import com.fxz.common.Idempotent.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.fxz.common.Idempotent.redis.IdempotentRedisService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-04-11 22:12
 */
@SuppressWarnings("all")
@AutoConfiguration
public class IdempotentAutoConfig {

	@Bean
	public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
		return new DefaultIdempotentKeyResolver();
	}

	@Bean
	public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
		return new ExpressionIdempotentKeyResolver();
	}

	@Bean
	public IdempotentRedisService idempotentRedisDAO(StringRedisTemplate RedisTemplate) {
		return new IdempotentRedisService(RedisTemplate);
	}

	@Bean
	public IdempotentAspect idempotentAspect(IdempotentRedisService idempotentRedisService) {
		return new IdempotentAspect(idempotentRedisService);
	}

}
