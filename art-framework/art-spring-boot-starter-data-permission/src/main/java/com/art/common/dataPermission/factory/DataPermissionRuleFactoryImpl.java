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

package com.art.common.dataPermission.factory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.art.common.dataPermission.annotation.DataPermission;
import com.art.common.dataPermission.local.DataPermissionContextHolder;
import com.art.common.dataPermission.rule.DataPermissionRule;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 默认的数据权限规则工厂实现类 支持通过DataPermissionContextHolder过滤数据权限 获取生效的数据权限规则
 *
 * @author fxz
 */
@RequiredArgsConstructor
public class DataPermissionRuleFactoryImpl implements DataPermissionRuleFactory {

	/**
	 * 容器中的数据权限规则数组
	 */
	private final List<DataPermissionRule> rules;

	/**
	 * 获取生效的数据权限规则
	 * @return 生效的数据权限规则数组
	 */
	@Override
	public List<DataPermissionRule> getDataPermissionRule() {
		// 通过上下文获取数据权限注解
		DataPermission dataPermission = DataPermissionContextHolder.get();

		// 容器中没有数据权限规则的配置
		if (CollUtil.isEmpty(rules)) {
			// 返回空
			return Collections.emptyList();
		}

		// 没有配置数据权限信息 则默认开启 生效所有规则
		if (Objects.isNull(dataPermission)) {
			// 返回容器中配置的所有数据权限规则
			return rules;
		}

		// 已配置数据权限信息 但是禁用 返回空
		if (!dataPermission.enable()) {
			// 返回空
			return Collections.emptyList();
		}

		// 已配置数据权限信息 但是仅生效部分规则
		if (ArrayUtil.isNotEmpty(dataPermission.includeRules())) {
			// 过滤出生效的数据权限规则
			return rules.stream()
				.filter(rule -> ArrayUtil.contains(dataPermission.includeRules(), rule.getClass()))
				.collect(Collectors.toList());
		}

		// 已配置数据权限 但是排除部分规则
		if (ArrayUtil.isNotEmpty(dataPermission.excludeRules())) {
			// 过滤出需要排除的数据权限规则
			return rules.stream()
				.filter(rule -> !ArrayUtil.contains(dataPermission.excludeRules(), rule.getClass()))
				.collect(Collectors.toList());
		}

		// 已配置数据权限信息 全部规则生效
		return rules;
	}

}
