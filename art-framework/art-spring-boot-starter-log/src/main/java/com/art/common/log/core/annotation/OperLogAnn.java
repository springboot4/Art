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

package com.art.common.log.core.annotation;

import com.art.common.log.core.enums.BusinessType;

import java.lang.annotation.*;

/**
 * 操作日志记录注解
 *
 * @author fxz
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLogAnn {

	/**
	 * 操作模块
	 */
	String title() default "";

	/**
	 * 业务操作类型
	 */
	BusinessType businessType() default BusinessType.OTHER;

	/**
	 * 是否保存请求的参数
	 */
	boolean isSaveRequestData() default true;

}
