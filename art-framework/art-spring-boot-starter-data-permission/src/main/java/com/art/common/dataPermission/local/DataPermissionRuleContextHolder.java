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

package com.art.common.dataPermission.local;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.art.common.dataPermission.rule.DataPermissionRule;

import java.util.List;

/**
 * SQL 解析上下文 方便透传 {@link DataPermissionRule} 规则
 *
 * @author fxz
 */

public class DataPermissionRuleContextHolder {

	/**
	 * MappedStatement 对应的规则
	 */
	private static final ThreadLocal<List<DataPermissionRule>> RULES = new TransmittableThreadLocal<>();

	/**
	 * SQL 是否进行重写
	 */
	private static final ThreadLocal<Boolean> REWRITE = new TransmittableThreadLocal<>();

	public static void init(List<DataPermissionRule> rules) {
		RULES.set(rules);
		REWRITE.set(false);
	}

	public static void clear() {
		RULES.remove();
		REWRITE.remove();
	}

	public static boolean getRewrite() {
		return REWRITE.get();
	}

	public static void setRewrite(boolean rewrite) {
		REWRITE.set(rewrite);
	}

	public static List<DataPermissionRule> getRules() {
		return RULES.get();
	}

}