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

package com.art.gen.service.impl;

import com.art.gen.dao.dataobject.DatabaseColumnDO;
import com.art.gen.dao.dataobject.DatabaseTableDO;
import com.art.gen.dao.mysql.DatabaseTableMapper;
import com.art.gen.service.DatabaseTableService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-03-03 15:55
 */
@Service
@RequiredArgsConstructor
public class DatabaseTableServiceImpl implements DatabaseTableService {

	private final DatabaseTableMapper databaseTableMapper;

	/**
	 * 获取表基本信息
	 */
	@DS("#last")
	@Override
	public DatabaseTableDO findByTableName(String tableName, String dsName) {
		return databaseTableMapper.findByTableName(tableName);
	}

	/**
	 * 获取表的列信息
	 */
	@DS("#last")
	@Override
	public List<DatabaseColumnDO> findColumnByTableName(String tableName, String dsName) {
		return databaseTableMapper.findColumnByTableName(tableName);
	}

	/**
	 * 分页查询基础表信息
	 * @param dsName 查询的数据库
	 * @return 分页信息
	 */
	@DS("#last")
	public IPage<DatabaseTableDO> page(Integer current, Integer size, String tableName, String dsName) {
		Page<DatabaseTableDO> page = new Page<>(current, size);
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""),
				DatabaseTableDO.class);
		return databaseTableMapper.page(page, Wrappers.<DatabaseTableDO>lambdaQuery()
			.like(StringUtils.isNotEmpty(tableName), DatabaseTableDO::getTableName, tableName));
	}

}
