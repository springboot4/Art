package com.fxz.common.Idempotent.keyresolver;

import com.fxz.common.Idempotent.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;

/**
 * @author fxz
 */
public interface KeyResolver {

	/**
	 * 解析幂等key
	 * @param idempotent 幂等注解
	 * @param joinPoint 连接点
	 * @return 解析出的幂等key
	 */
	String resolver(JoinPoint joinPoint, Idempotent idempotent);

}
