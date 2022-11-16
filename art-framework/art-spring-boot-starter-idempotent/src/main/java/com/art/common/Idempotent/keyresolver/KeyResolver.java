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

package com.art.common.Idempotent.keyresolver;

import com.art.common.Idempotent.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;

/**
 * @author fxz
 */
public interface KeyResolver {

	/**
	 * 解析幂等key
	 * @param idempotent 幂等注解
	 * @param joinPoint 连接点
	 * @return 解析出的幂等key
	 */
	String resolver(JoinPoint joinPoint, Idempotent idempotent);

}
