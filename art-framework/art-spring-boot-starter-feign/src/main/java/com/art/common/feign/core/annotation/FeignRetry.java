package com.art.common.feign.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试注解
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignRetry {

	/**
	 * 重试策略
	 */
	Backoff backoff() default @Backoff();

	/**
	 * 最大尝试次数
	 */
	int maxAttempt() default 3;

	Class<? extends Throwable>[] include() default {};

}
