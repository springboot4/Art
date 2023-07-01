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

package com.art.common.tenant.aspect;

import com.art.common.tenant.context.TenantContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/1 13:43
 */
@Aspect
public class IgnoreTenantAspect implements Ordered {

	@Around("@annotation(ignoreTenant)")
	public Object around(ProceedingJoinPoint joinPoint, IgnoreTenant ignoreTenant) throws Throwable {
		// 租户配置原始信息
		Boolean ignore = TenantContextHolder.isIgnore();
		try {
			// 设置忽略租户上下文
			TenantContextHolder.setIgnore(true);
			// 执行逻辑
			return joinPoint.proceed();
		}
		finally {
			// 恢复现场
			TenantContextHolder.setIgnore(ignore);
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}

}
