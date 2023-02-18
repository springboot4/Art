/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.database.core.config;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 动态解析数据源 取最后一个参数
 *
 * @author fxz @DS("#last")
 */
public class DsLastParamProcessor extends DsProcessor {

	private static final String LAST_PREFIX = "#last";

	/**
	 * 匹配才会执行，否则走下一级执行器 默认有三个职责链来处理动态参数解析器 header->session->spel
	 * @param key
	 * @return 是否匹配当前执行器
	 */
	@Override
	public boolean matches(String key) {
		return key.startsWith(LAST_PREFIX);
	}

	/**
	 * 抽象最终决定数据源,这里取方法最后一个参数
	 * @param invocation 方法执行信息
	 * @param key DS注解里的内容
	 * @return 数据源名称
	 */
	@Override
	public String doDetermineDatasource(MethodInvocation invocation, String key) {
		return String.valueOf(invocation.getArguments()[invocation.getArguments().length - 1]);
	}

}