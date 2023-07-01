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

package com.art.common.feign.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试策略
 *
 * @author fxz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArtBackoff {

	/**
	 * 重试延迟时间 单位毫秒 默认值1000 即默认延迟1秒
	 * <p/>
	 * 当未设置multiplier时 表示每隔value的时间重试 直到重试次数到达maxAttempts设置的最大允许重试次数
	 * <p/>
	 * 当设置了multiplier参数时 该值作为幂运算的初始值
	 */
	long delay() default 1000L;

	/**
	 * 两次重试间最大间隔时间
	 * <p/>
	 * 当设置multiplier参数后 下次延迟时间根据是上次延迟时间乘以 multiplier得出的 这会导致两次重试间的延迟时间越来越长
	 * <p/>
	 * 该参数限制两次重试的最大间隔时间 当间隔时间大于该值时 计算出的间隔时间将会被忽略 使用上次的重试间隔时间
	 */
	long maxDelay() default 0L;

	/**
	 * 指定延迟倍数 作为乘数用于计算下次延迟时间 公式：delay = delay * multiplier
	 */
	double multiplier() default 0.0D;

}
