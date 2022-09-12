package com.fxz.common.Idempotent.keyresolver.impl;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.fxz.common.Idempotent.annotation.Idempotent;
import com.fxz.common.Idempotent.keyresolver.KeyResolver;
import org.aspectj.lang.JoinPoint;

/**
 * 默认的key解析器 根据方法名和参数生成key
 *
 * @author fxz
 */
public class DefaultIdempotentKeyResolver implements KeyResolver {

	@Override
	public String resolver(JoinPoint joinPoint, Idempotent idempotent) {
		String methodName = joinPoint.getSignature().toString();
		String argsStr = StrUtil.join(StrPool.COMMA, joinPoint.getArgs());
		return methodName + argsStr;
	}

}
