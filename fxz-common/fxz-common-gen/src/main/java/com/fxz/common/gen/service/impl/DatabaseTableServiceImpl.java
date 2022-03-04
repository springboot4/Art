package com.fxz.common.gen.service.impl;

import com.fxz.common.gen.entity.DatabaseColumn;
import com.fxz.common.gen.entity.DatabaseTable;
import com.fxz.common.gen.mapper.DatabaseTableMapper;
import com.fxz.common.gen.service.DatabaseTableService;
import lombok.RequiredArgsConstructor;
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
	@Override
	public DatabaseTable findByTableName(String tableName) {
		return databaseTableMapper.findByTableName(tableName);
	}

	/**
	 * 获取表的列信息
	 */
	@Override
	public List<DatabaseColumn> findColumnByTableName(String tableName) {
		return databaseTableMapper.findColumnByTableName(tableName);
	}

}
