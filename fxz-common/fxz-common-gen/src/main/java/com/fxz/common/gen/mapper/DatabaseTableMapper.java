package com.fxz.common.gen.mapper;

import com.fxz.common.gen.entity.DatabaseColumn;
import com.fxz.common.gen.entity.DatabaseTable;
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

}
