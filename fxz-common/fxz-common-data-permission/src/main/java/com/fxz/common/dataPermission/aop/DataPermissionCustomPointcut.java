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

import com.fxz.common.dataPermission.annotation.DataPermission;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * 自定义Pointcut
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/9/10 11:09
 */
public class DataPermissionCustomPointcut {

	/**
	 * 自定义切入点 复合切点 为创建多个切点而提供的方便操作类 它所有的方法都返回ComposablePointcut类 这样，我们就可以使用链接表达式对其进行操作
	 */
	protected static Pointcut of() {
		Pointcut classPointcut = new AnnotationMatchingPointcut(DataPermission.class, true);
		Pointcut methodPointcut = new AnnotationMatchingPointcut(null, DataPermission.class, true);
		return new ComposablePointcut(classPointcut).union(methodPointcut);
	}

}
