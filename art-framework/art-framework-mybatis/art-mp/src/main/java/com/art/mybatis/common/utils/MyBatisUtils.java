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

package com.art.mybatis.common.utils;

import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MyBatis 工具类
 *
 * @author fxz
 */
public class MyBatisUtils {

	private static final String MYSQL_ESCAPE_CHARACTER = "`";

	/**
	 * 将拦截器添加到链中
	 * @param interceptor 链
	 * @param inner 拦截器
	 * @param index 位置
	 */
	public static void addInterceptor(MybatisPlusInterceptor interceptor, InnerInterceptor inner, int index) {
		List<InnerInterceptor> inners = new ArrayList<>(interceptor.getInterceptors());
		inners.add(index, inner);
		interceptor.setInterceptors(inners);
	}

	/**
	 * 获得 Table 对应的表名
	 * <p>
	 * 兼容 MySQL 转义表名 `t_xxx`
	 * @param table 表
	 * @return 去除转移字符后的表名
	 */
	public static String getTableName(Table table) {
		String tableName = table.getName();
		if (tableName.startsWith(MYSQL_ESCAPE_CHARACTER) && tableName.endsWith(MYSQL_ESCAPE_CHARACTER)) {
			tableName = tableName.substring(1, tableName.length() - 1);
		}
		return tableName;
	}

	/**
	 * 构建 Column 对象
	 * @param tableName 表名
	 * @param tableAlias 别名
	 * @param column 字段名
	 * @return Column 对象
	 */
	public static Column buildColumn(String tableName, Alias tableAlias, String column) {
		return new Column(Objects.nonNull(tableAlias) ? tableAlias.getName() + StringPool.DOT + column : column);
	}

}
