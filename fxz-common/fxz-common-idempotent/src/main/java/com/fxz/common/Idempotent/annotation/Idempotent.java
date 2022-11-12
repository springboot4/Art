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

package com.fxz.common.Idempotent.annotation;

import com.fxz.common.Idempotent.keyresolver.KeyResolver;
import com.fxz.common.Idempotent.keyresolver.impl.DefaultIdempotentKeyResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 幂等注解
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022-04-11 21:20
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

	/**
	 * 幂等key
	 */
	String key() default "";

	/**
	 * 幂等的超时时间
	 */
	int timeout() default 1;

	/**
	 * 时间单位
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	/**
	 * 提示信息
	 */
	String message() default "请求重复，请稍后重试!";

	/**
	 * 业务执行完成是否删除key
	 */
	boolean delKey() default false;

	/**
	 * key 解析器
	 */
	Class<? extends KeyResolver> keyResolver() default DefaultIdempotentKeyResolver.class;

}
