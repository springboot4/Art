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

package com.art.common.dataPermission.aop;

import com.art.common.dataPermission.annotation.DataPermission;
import com.art.common.dataPermission.local.DataPermissionContextHolder;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义Advice 处理数据权限的通知
 * <p/>
 * 1. 在执行方法前 将数据权限注解入栈 2. 在执行方法后 将数据权限注解出栈
 *
 * @author fxz
 */
@DataPermission
@AllArgsConstructor(staticName = "of")
public class DataPermissionCustomAdvice implements MethodInterceptor {

	/**
	 * 方法无数据权限注解时 使用此进行占位
	 */
	static final DataPermission DATA_PERMISSION_NULL = DataPermissionCustomAdvice.class
			.getAnnotation(DataPermission.class);

	private final Map<MethodClassKey, DataPermission> dataPermissionCache = new ConcurrentHashMap<>();

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		// 方法执行前 获取方法上的数据权限注解
		DataPermission dataPermission = this.findAnnotation(methodInvocation);

		if (Objects.nonNull(dataPermission)) {
			// 数据权限注解入栈
			DataPermissionContextHolder.add(dataPermission);
		}

		try {
			// 执行目标方法
			return methodInvocation.proceed();
		}
		finally {
			if (Objects.nonNull(dataPermission)) {
				// 方法执行后 数据权限注解出栈
				DataPermissionContextHolder.remove();
			}
		}
	}

	private DataPermission findAnnotation(MethodInvocation methodInvocation) {
		Method method = methodInvocation.getMethod();
		Object targetObject = methodInvocation.getThis();
		Class<?> clazz = Objects.nonNull(targetObject) ? targetObject.getClass() : method.getDeclaringClass();
		MethodClassKey methodClassKey = new MethodClassKey(method, clazz);

		// 从缓存中获取数据权限注解
		DataPermission dataPermission = dataPermissionCache.get(methodClassKey);
		if (Objects.nonNull(dataPermission)) {
			// 缓存中已经存在此方法的数据权限信息 直接返回
			return dataPermission != DATA_PERMISSION_NULL ? dataPermission : null;
		}

		// 优先从方法中获取注解
		dataPermission = AnnotationUtils.findAnnotation(method, DataPermission.class);
		if (Objects.isNull(dataPermission)) {
			// 方法上没有 从类上获取
			dataPermission = AnnotationUtils.findAnnotation(clazz, DataPermission.class);
		}

		// 方法或者类上有注解则添加到缓存中 没有则添加默认的到缓存
		dataPermissionCache.put(methodClassKey,
				Objects.nonNull(dataPermission) ? dataPermission : DATA_PERMISSION_NULL);

		return dataPermission;
	}

}
