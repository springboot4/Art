package com.fxz.gen.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.common.core.param.PageParam;
import com.fxz.gen.entity.DatabaseColumn;
import com.fxz.gen.entity.DatabaseTable;
import com.fxz.gen.mapper.DatabaseTableMapper;
import com.fxz.gen.service.DatabaseTableService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
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
	public DatabaseTable findByTableName(String tableName, String dsName) {
		return databaseTableMapper.findByTableName(tableName);
	}

	/**
	 * 获取表的列信息
	 */
	@DS("#last")
	@Override
	public List<DatabaseColumn> findColumnByTableName(String tableName, String dsName) {
		return databaseTableMapper.findColumnByTableName(tableName);
	}

	/**
	 * 分页查询基础表信息
	 * @param pageParam 分页参数
	 * @param param 查询参数
	 * @param dsName 查询的数据库
	 * @return 分页信息
	 */
	@DS("#last")
	public IPage<DatabaseTable> page(PageParam pageParam, DatabaseTable param, String dsName) {
		Page<DatabaseTable> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
		// 实体类entity及其字段没有缓存或者说指定字段没有缓存
		// https://blog.csdn.net/qq_36491545/article/details/109091325
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DatabaseTable.class);
		return databaseTableMapper.page(page, Wrappers.<DatabaseTable>lambdaQuery()
				.like(StringUtils.isNotEmpty(param.getTableName()), DatabaseTable::getTableName, param.getTableName()));
	}

}
