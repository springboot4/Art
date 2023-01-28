package com.art.common.feign.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试注解
 *
 * @author fxz
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ArtFeignRetry {

	/**
	 * 重试策略 我们使用自定义的{@link ArtBackoff}
	 */
	ArtBackoff backoff() default @ArtBackoff();

	/**
	 * 最大重试次数 默认3次
	 */
	int maxAttempt() default 3;

	/**
	 * 抛出指定异常才会重试
	 */
	Class<? extends Throwable>[] include() default {};

}
