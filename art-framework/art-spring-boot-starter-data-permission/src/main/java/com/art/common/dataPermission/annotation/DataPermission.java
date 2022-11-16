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

package com.art.common.dataPermission.annotation;

import com.art.common.dataPermission.rule.DataPermissionRule;

import java.lang.annotation.*;

/**
 * 数据权限注解 可声明在类或者方法上 标识使用的数据权限规则
 *
 * @author fxz
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

	/**
	 * 是否开启数据权限 即使不添加注解 默认是开启状态 可通过设置 enable 为 false 禁用
	 */
	boolean enable() default true;

	/**
	 * 生效的数据权限规则数组 优先级高于 {@link #excludeRules()}
	 */
	Class<? extends DataPermissionRule>[] includeRules() default {};

	/**
	 * 排除的数据权限规则数组 优先级低与 {@link #includeRules()}
	 */
	Class<? extends DataPermissionRule>[] excludeRules() default {};

}
