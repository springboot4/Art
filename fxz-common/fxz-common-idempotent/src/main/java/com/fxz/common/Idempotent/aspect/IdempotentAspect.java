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

package com.fxz.common.Idempotent.aspect;

import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fxz.common.Idempotent.annotation.Idempotent;
import com.fxz.common.Idempotent.constant.IdempotentConstant;
import com.fxz.common.Idempotent.keyresolver.KeyResolver;
import com.fxz.common.Idempotent.redis.IdempotentRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 幂等切面
 *
 * @author Fxz
 * @version 1.0
 * @date 2022-04-11 21:22
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class IdempotentAspect {

	/**
	 * redisTemplate封装
	 */
	private final IdempotentRedisService idempotentRedisService;

	/**
	 * 缓存幂等注解信息
	 */
	private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

	/**
	 * 使用前置通知,对带有Idempotent注解的进行幂等处理
	 */
	@Before("@annotation(idempotent)")
	public void beforePointCut(JoinPoint joinPoint, Idempotent idempotent) {
		// 获取解析器
		KeyResolver keyResolver = SpringUtil.getBean(idempotent.keyResolver());
		Assert.notNull(keyResolver, "找不到对应的幂等解析器！");

		// 解析 key
		String key = keyResolver.resolver(joinPoint, idempotent);

		// 锁定 key
		boolean success = idempotentRedisService.setIfAbsent(key, idempotent.timeout(), idempotent.timeUnit());

		// 锁定失败，抛出异常
		if (!success) {
			log.info("方法:{} 参数:{} 重复请求!", joinPoint.getSignature().toString(), joinPoint.getArgs());
			throw new RuntimeException(idempotent.message());
		}

		// 缓存注解信息
		Map<String, Object> map = THREAD_LOCAL.get();
		map.put(IdempotentConstant.KEY_PREFIX, key);
		map.put(IdempotentConstant.DEL_KEY_PREFIX, idempotent.delKey());
	}

	/**
	 * 使用后置通知,对业务执行完成，需要删除的key进行删除
	 */
	@After("@annotation(idempotent)")
	public void afterPointCut(JoinPoint joinPoint, Idempotent idempotent) {
		// 获取缓存的幂等注解信息
		Map<String, Object> map = THREAD_LOCAL.get();
		if (MapUtil.isEmpty(map)) {
			return;
		}

		String key = (String) map.get(IdempotentConstant.KEY_PREFIX);
		Boolean delKey = (Boolean) map.get(IdempotentConstant.DEL_KEY_PREFIX);
		if (!delKey) {
			return;
		}

		// 删除幂等key
		if (!idempotentRedisService.removeKey(key)) {
			log.info("删除幂等key:{}失败！", key);
		}

		// 删除缓存的幂等注解信息
		THREAD_LOCAL.remove();
	}

}
