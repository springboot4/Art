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

package com.art.common.security.core.support;

import cn.hutool.core.util.StrUtil;
import com.art.common.core.constant.SecurityConstants;
import com.art.common.security.core.annotation.Ojbk;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author fxz
 * <p>
 * 内部服务调用是否鉴权切面
 */
@SuppressWarnings("all")
@Slf4j
@Aspect
@RequiredArgsConstructor
public class SecurityInnerAspect implements Ordered {

	private final HttpServletRequest request;

	/**
	 * 前置处理Ojbk注解 判断是否为服务内部调用
	 * @param point 连接点
	 * @param ojbk Ojbk注解
	 */
	@SneakyThrows
	@Before("@within(ojbk) || @annotation(ojbk)")
	public void before(JoinPoint point, Ojbk ojbk) {
		// 实际注入的Ojbk实体由表达式后一个注解决定 即取方法上的注解 若方法上无注解 则获取类上的
		if (ojbk == null) {
			Class<?> clazz = point.getTarget().getClass();
			ojbk = AnnotationUtils.findAnnotation(clazz, Ojbk.class);
		}

		String header = request.getHeader(SecurityConstants.FROM);
		if (ojbk.inner() && !StrUtil.equals(SecurityConstants.FROM_IN, header)) {
			log.warn("访问接口 {} 没有权限", point.getSignature().getName());
			throw new AccessDeniedException("Access is denied");
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}

}
