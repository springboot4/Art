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

package com.fxz.common.Idempotent.keyresolver.impl;

import cn.hutool.core.util.ArrayUtil;
import com.fxz.common.Idempotent.annotation.Idempotent;
import com.fxz.common.Idempotent.keyresolver.KeyResolver;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * key解析器 使用spring el生成key
 *
 * @author fxz
 */
public class ExpressionIdempotentKeyResolver implements KeyResolver {

	/**
	 * 参数解析器
	 */
	private final ExpressionParser expressionParser = new SpelExpressionParser();

	/**
	 * 获取方法参数名
	 */
	private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	@Override
	public String resolver(JoinPoint joinPoint, Idempotent idempotent) {
		// 获得被拦截方法
		Method method = getMethod(joinPoint);

		// 获取方法参数
		Object[] args = joinPoint.getArgs();

		// 获取到参数名
		String[] parameterNames = this.parameterNameDiscoverer.getParameterNames(method);

		// el表达式解析上下文
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		if (ArrayUtil.isNotEmpty(parameterNames)) {
			for (int i = 0; i < parameterNames.length; i++) {
				evaluationContext.setVariable(parameterNames[i], args[i]);
			}
		}

		// 解析参数
		Expression expression = expressionParser.parseExpression(idempotent.key());

		// 返回解析的key值
		return expression.getValue(evaluationContext, String.class);
	}

	private static Method getMethod(JoinPoint point) {
		// 获取被拦截的方法
		Method method = ((MethodSignature) point.getSignature()).getMethod();

		// 声明在类上的情况 直接返回
		if (!method.getDeclaringClass().isInterface()) {
			return method;
		}

		// 声明在接口上的情况 根据实际执行的类处理
		try {
			return point.getTarget().getClass().getDeclaredMethod(point.getSignature().getName(),
					method.getParameterTypes());
		}
		catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

}
