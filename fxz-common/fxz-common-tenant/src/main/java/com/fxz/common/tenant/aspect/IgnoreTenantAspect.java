package com.fxz.common.tenant.aspect;

import com.fxz.common.tenant.context.TenantContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

/**
 * @author Fxz
 * @version 1.0
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
