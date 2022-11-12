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

package com.fxz.common.Idempotent.keyresolver.impl;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.fxz.common.Idempotent.annotation.Idempotent;
import com.fxz.common.Idempotent.keyresolver.KeyResolver;
import org.aspectj.lang.JoinPoint;

/**
 * 默认的key解析器 根据方法名和参数生成key
 *
 * @author fxz
 */
public class DefaultIdempotentKeyResolver implements KeyResolver {

	@Override
	public String resolver(JoinPoint joinPoint, Idempotent idempotent) {
		String methodName = joinPoint.getSignature().toString();
		String argsStr = StrUtil.join(StrPool.COMMA, joinPoint.getArgs());
		return methodName + argsStr;
	}

}
