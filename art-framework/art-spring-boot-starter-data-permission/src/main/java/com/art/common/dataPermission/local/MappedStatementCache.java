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

import cn.hutool.core.collection.CollUtil;
import com.art.common.dataPermission.rule.DataPermissionRule;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link MappedStatement} 缓存 目前主要用于 记录 {@link DataPermissionRule} 是否对指定
 * {@link MappedStatement} 无效 如果无效 则可以避免 SQL 的解析 加快速度
 *
 * @author fxz
 */
public class MappedStatementCache {

	/**
	 * 指定数据权限规则，对指定 MappedStatement 无需重写（不生效)的缓存
	 * <p>
	 * value：{@link MappedStatement#getId()} 编号
	 */
	private final Map<Class<? extends DataPermissionRule>, Set<String>> noRewritableMappedStatements = new ConcurrentHashMap<>();

	/**
	 * 判断是否无需重写
	 * @param ms MappedStatement
	 * @param rules 数据权限规则数组
	 * @return 是否无需重写
	 */
	public boolean noRewritable(MappedStatement ms, List<DataPermissionRule> rules) {
		// 如果规则为空 无需重写
		if (CollUtil.isEmpty(rules)) {
			return true;
		}

		for (DataPermissionRule rule : rules) {
			// 根据规则类获取不需要重写的MappedStatementId集合
			Set<String> mappedStatementIds = noRewritableMappedStatements.get(rule.getClass());
			// 任一规则不在 noRewritableMap 中 则说明可能需要重写
			if (!CollUtil.contains(mappedStatementIds, ms.getId())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 添加无需重写的 MappedStatement
	 * @param ms MappedStatement
	 * @param rules 数据权限规则数组
	 */
	public void addNoRewritable(MappedStatement ms, List<DataPermissionRule> rules) {
		for (DataPermissionRule rule : rules) {
			Set<String> mappedStatementIds = noRewritableMappedStatements.get(rule.getClass());
			if (CollUtil.isNotEmpty(mappedStatementIds)) {
				mappedStatementIds.add(ms.getId());
			}
			else {
				noRewritableMappedStatements.put(rule.getClass(), new HashSet<>(Collections.singletonList(ms.getId())));
			}
		}
	}

}