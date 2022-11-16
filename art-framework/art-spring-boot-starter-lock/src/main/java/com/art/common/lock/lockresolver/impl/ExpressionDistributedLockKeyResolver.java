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

package com.art.common.lock.lockresolver.impl;

import cn.hutool.core.util.ArrayUtil;
import com.art.common.lock.annotation.DistributedLock;
import com.art.common.lock.entity.LockEntity;
import com.art.common.lock.lockresolver.DistributedLockResolver;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * spring el表达式key解析器
 *
 * @author fxz
 */
@SuppressWarnings("all")
public class ExpressionDistributedLockKeyResolver implements DistributedLockResolver {

	/**
	 * 参数解析器
	 */
	private final ExpressionParser expressionParser = new SpelExpressionParser();

	/**
	 * 获取方法参数名
	 */
	private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	@Override
	public LockEntity resolver(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
		// 获得被拦截方法参数名列表
		Method method = getMethod(joinPoint);
		// 获取方法参数
		Object[] args = joinPoint.getArgs();
		// 获取到参数名
		String[] parameterNames = this.parameterNameDiscoverer.getParameterNames(method);

		// 准备 Spring EL 表达式解析的上下文
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		if (ArrayUtil.isNotEmpty(parameterNames)) {
			for (int i = 0; i < parameterNames.length; i++) {
				evaluationContext.setVariable(parameterNames[i], args[i]);
			}
		}

		// 解析参数
		Expression expression = expressionParser.parseExpression(distributedLock.lockName());

		return new LockEntity().setLockName(expression.getValue(evaluationContext, String.class))
				.setWaitTime(distributedLock.waitTime()).setTimeUnit(distributedLock.timeUnit());
	}

	private static Method getMethod(JoinPoint point) {
		// 处理，声明在类上的情况
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		if (!method.getDeclaringClass().isInterface()) {
			return method;
		}

		// 处理，声明在接口上的情况
		try {
			return point.getTarget().getClass().getDeclaredMethod(point.getSignature().getName(),
					method.getParameterTypes());
		}
		catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

}
