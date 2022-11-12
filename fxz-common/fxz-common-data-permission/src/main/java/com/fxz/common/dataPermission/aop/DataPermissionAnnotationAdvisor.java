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

package com.fxz.common.dataPermission.aop;

import lombok.Getter;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * Advisor通常由另两个组件组成——Advice接口和Pointcut接口 其中Advice表示实际增强的逻辑入口(通知)
 * Pointcut表示哪些类或者哪些方法需要被拦截(切入点)
 * <p/>
 * 切点切面PointcutAdvisor 代表具有切点的切面 它可以通过任意Pointcut和Advice定义一个切面
 * 这样就可以通过类、方法名以及方位等信息灵活的定义切面的连接点 提供更具实用性的切面
 *
 * @author fxz
 */
@Getter
public class DataPermissionAnnotationAdvisor extends DefaultPointcutAdvisor {

	private final Advice advice;

	private final Pointcut pointcut;

	public DataPermissionAnnotationAdvisor() {
		// 自定义通知
		this.advice = DataPermissionCustomAdvice.of();
		// 自定义切入点
		this.pointcut = DataPermissionCustomPointcut.of();
	}

}
