package com.fxz.common.gen.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.fxz.common.gen.config.GenConfig;
import com.fxz.common.gen.dto.CodeGenPreview;
import com.fxz.common.gen.entity.CodeGenColumn;
import com.fxz.common.gen.entity.DatabaseColumn;
import com.fxz.common.gen.entity.DatabaseTable;
import com.fxz.common.gen.enums.CodeGenColumnTypeEnum;
import com.fxz.common.gen.enums.CodeGenTemplateVmEnum;
import com.fxz.common.gen.service.CodeGeneratorService;
import com.fxz.common.gen.util.VelocityInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 15:40
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

	private final DatabaseTableServiceImpl databaseTableService;

	/**
	 * 生成预览代码
	 * @param tableName 表名
	 * @return 预览代码
	 */
	@Override
	public List<CodeGenPreview> codeGenPreview(String tableName) {
		VelocityInitializer.initVelocity();

		// 获取生成代码所用的数据
		Map<String, Object> map = this.getCodeGenInfo(tableName);
		// 遍历生成代码预览
		return Arrays.stream(CodeGenTemplateVmEnum.values()).map(vmEnum -> {
			VelocityContext context = new VelocityContext(map);
			StringWriter sw = new StringWriter();
			Template template = Velocity.getTemplate(vmEnum.getPath(), CharsetUtil.UTF_8);
			template.merge(context, sw);
			return new CodeGenPreview().setName(vmEnum.getName()).setContent(sw.toString());
		}).collect(Collectors.toList());
	}

	/**
	 * 获取生成代码所用的数据
	 * @param tableName 表名
	 */
	private Map<String, Object> getCodeGenInfo(String tableName) {
		// 获取表的基本信息
		DatabaseTable databaseTable = databaseTableService.findByTableName(tableName);
		// 获取表的列信息
		List<DatabaseColumn> databaseColumns = databaseTableService.findColumnByTableName(tableName);

		Map<String, Object> map = new HashMap<>(16);

		// 数据库字段转实体
		List<CodeGenColumn> columns = databaseColumns.stream().map(databaseColumn -> new CodeGenColumn()
				// 备注
				.setComments(databaseColumn.getColumnComment())
				// 数据库字段类型转java数据类型
				.setAttrType(CodeGenColumnTypeEnum.convertJavaType(databaseColumn.getDataType()))
				// 驼峰
				.setName(NamingCase.toCamelCase(databaseColumn.getColumnName()))).collect(Collectors.toList());

		// 添加代码生成所需要的属性
		map.put("comments", databaseTable.getTableComment());
		map.put("datetime", DateUtil.formatDate(new Date()));
		map.put("className", tableToJava(databaseTable.getTableName()));
		map.put("classname", StrUtil.lowerFirst(tableToJava(databaseTable.getTableName())));
		map.put("classNameKebab", NamingCase.toKebabCase(tableToJava(databaseTable.getTableName())));
		map.put("tableName", databaseTable.getTableName());
		map.put("columns", columns);
		map.put("module", GenConfig.module);
		map.put("author", GenConfig.author);

		return map;
	}

	/**
	 * 表名转换成Java类名 大驼峰
	 */
	public static String tableToJava(String tableName) {
		// 自动去除表前缀
		return NamingCase.toPascalCase(tableName.substring(tableName.indexOf("_") + 1));
	}

}
