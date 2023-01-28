/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.feign.core.aspect;

import com.art.common.feign.core.annotation.FeignRetry;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
public class FeignRetryAspect {

	@Around("@annotation(com.art.common.feign.core.annotation.FeignRetry)")
	public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
		Method method = getCurrentMethod(joinPoint);
		FeignRetry feignRetry = method.getAnnotation(FeignRetry.class);

		RetryTemplate retryTemplate = new RetryTemplate();
		// Setter for BackOffPolicy.
		retryTemplate.setBackOffPolicy(prepareBackOffPolicy(feignRetry));
		// Setter for RetryPolicy.
		retryTemplate.setRetryPolicy(prepareSimpleRetryPolicy(feignRetry));

		// 重试
		return retryTemplate.execute(arg0 -> {
			int retryCount = arg0.getRetryCount();
			log.info("Sending request method: {}, max attempt: {}, delay: {}, retryCount: {}", method.getName(),
					feignRetry.maxAttempt(), feignRetry.backoff().delay(), retryCount);
			return joinPoint.proceed(joinPoint.getArgs());
		});
	}

	private BackOffPolicy prepareBackOffPolicy(FeignRetry feignRetry) {
		if (feignRetry.backoff().multiplier() != 0) {
			ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
			backOffPolicy.setInitialInterval(feignRetry.backoff().delay());
			backOffPolicy.setMaxInterval(feignRetry.backoff().maxDelay());
			backOffPolicy.setMultiplier(feignRetry.backoff().multiplier());
			return backOffPolicy;
		}
		else {
			FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
			fixedBackOffPolicy.setBackOffPeriod(feignRetry.backoff().delay());
			return fixedBackOffPolicy;
		}
	}

	private SimpleRetryPolicy prepareSimpleRetryPolicy(FeignRetry feignRetry) {
		Map<Class<? extends Throwable>, Boolean> policyMap = new HashMap<>();
		policyMap.put(RetryableException.class, true);
		for (Class<? extends Throwable> t : feignRetry.include()) {
			policyMap.put(t, true);
		}
		return new SimpleRetryPolicy(feignRetry.maxAttempt(), policyMap, true);
	}

	/**
	 * 获取当前方法
	 * @param joinPoint 连接点
	 * @return {@link Method}
	 */
	private Method getCurrentMethod(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getMethod();
	}

}