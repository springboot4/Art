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

package com.fxz.common.lock.annotation;

import com.fxz.common.lock.constant.RedissonLockType;
import com.fxz.common.lock.lockresolver.DistributedLockResolver;
import com.fxz.common.lock.lockresolver.impl.DefaultDistributedLockKeyResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解(声明式加锁的方式暂不支持红锁、连锁)
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/4 23:12
 */
@Target(value = { ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DistributedLock {

	/**
	 * 锁的名称, 默认根据方法自动生成,可以使用SpEl表达式生成锁名称
	 */
	String lockName() default "";

	/**
	 * 使用的 Key 解析器
	 */
	Class<? extends DistributedLockResolver> lockResolver() default DefaultDistributedLockKeyResolver.class;

	/**
	 * 锁类型，默认为公平锁
	 */
	RedissonLockType lockType() default RedissonLockType.FAIR;

	/**
	 * 尝试加锁等待时间(秒)
	 */
	long waitTime() default 60L;

	/**
	 * 自动解锁时间(秒)
	 */
	long leaseTime() default 60L;

	/**
	 * 时间单位
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

}
