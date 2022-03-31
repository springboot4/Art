package com.fxz.gen.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.gen.entity.DatabaseColumn;
import com.fxz.gen.entity.DatabaseTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 15:55
 */
@Mapper
public interface DatabaseTableMapper {

	/**
	 * 获取表基本信息
	 */
	@Select("select table_name, engine, table_comment, create_time from information_schema.tables"
			+ "	where table_schema = (select database()) and table_name = #{tableName}")
	DatabaseTable findByTableName(@Param("tableName") String tableName);

	/**
	 * 获取表的列信息
	 */
	@Select("select column_name, data_type, column_comment, column_key from information_schema.columns"
			+ " where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
	List<DatabaseColumn> findColumnByTableName(String tableName);

	/**
	 * 分页查询基础表信息
	 */
	@Select("select table_name tableName,engine,table_comment tableComment,create_time createTime from (select table_name , engine, table_comment , create_time  from information_schema.tables"
			+ " where table_schema = (select database())) as t ${ew.customSqlSegment}")
	Page<DatabaseTable> page(Page<DatabaseTable> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

}
