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

package com.art.common.tenant.mp;

import cn.hutool.core.collection.CollUtil;
import com.art.common.tenant.config.FxzTenantProperties;
import com.art.common.tenant.context.TenantContextHolder;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;

import java.util.Objects;

/**
 * mybatis-plus 多租户插件配置
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/1 11:17
 */
public class TenantDatabaseHandler implements TenantLineHandler {

	private final FxzTenantProperties properties;

	/**
	 * 多租户的配置信息
	 * @param properties 多租户配置
	 */
	public TenantDatabaseHandler(FxzTenantProperties properties) {
		this.properties = properties;
	}

	/**
	 * 拼接租户条件
	 * @return 表达式
	 */
	@Override
	public Expression getTenantId() {
		Long tenantId = TenantContextHolder.getTenantId();
		if (Objects.isNull(tenantId)) {
			return new NullValue();
		}

		return new LongValue(tenantId);
	}

	/**
	 * 获取租户字段名
	 * @return 租户字段名
	 */
	@Override
	public String getTenantIdColumn() {
		return properties.getColumn();
	}

	/**
	 * 当前表是否忽略多租户
	 * @param tableName 表名
	 * @return true or false
	 */
	@Override
	public boolean ignoreTable(String tableName) {
		return TenantContextHolder.isIgnore() || !CollUtil.contains(properties.getTables(), tableName);
	}

}
