package com.art.common.feign.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试策略
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Backoff {

	/**
	 * 延时
	 */
	long delay() default 1000L;

	/**
	 * 最大延时
	 */
	long maxDelay() default 0L;

	double multiplier() default 0.0D;

}
