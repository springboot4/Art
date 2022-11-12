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

package com.fxz.common.dataPermission.rule;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;

import java.util.Set;

/**
 * 数据权限规则接口 通过实现接口 自定义数据规则
 *
 * @author fxz
 */
public interface DataPermissionRule {

	/**
	 * 返回需要生效的表名数组 DataPermission数组基于SQL重写 通过 Where 返回只有权限的数据
	 * <p>
	 * 如果需要基于实体名获得表名，可调用 {@link TableInfoHelper#getTableInfo(Class)} 获得
	 * @return 表名数组
	 */
	Set<String> getTableNames();

	/**
	 * 根据表名和别名 生成对应的 WHERE / OR 过滤条件
	 * @param tableName 表名
	 * @param tableAlias 别名 可能为空
	 * @return 过滤条件 Expression 表达式
	 */
	Expression getExpression(String tableName, Alias tableAlias);

}
