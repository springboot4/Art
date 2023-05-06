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

package com.art.gen.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.art.gen.core.config.GenConfig;
import com.art.gen.core.enums.CodeGenColumnTypeEnum;
import com.art.gen.core.enums.CodeGenTemplateVmEnum;
import com.art.gen.core.util.VelocityInitializer;
import com.art.gen.dao.dataobject.CodeGenColumnDO;
import com.art.gen.dao.dataobject.DatabaseColumnDO;
import com.art.gen.dao.dataobject.DatabaseTableDO;
import com.art.gen.core.dto.CodeGenPreviewDTO;
import com.art.gen.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Fxz
 * @version 0.0.1
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
	public List<CodeGenPreviewDTO> codeGenPreview(String tableName, String dsName) {
		VelocityInitializer.initVelocity();

		// 获取生成代码所用的数据
		Map<String, Object> map = this.getCodeGenInfo(tableName, dsName);
		// 遍历生成代码预览
		return Arrays.stream(CodeGenTemplateVmEnum.values()).map(vmEnum -> {
			VelocityContext context = new VelocityContext(map);
			StringWriter sw = new StringWriter();
			Template template = Velocity.getTemplate(vmEnum.getPath(), CharsetUtil.UTF_8);
			template.merge(context, sw);
			return new CodeGenPreviewDTO().setName(vmEnum.getName()).setContent(sw.toString());
		}).collect(Collectors.toList());
	}

	/**
	 * 获取生成代码所用的数据
	 * @param tableName 表名
	 */
	private Map<String, Object> getCodeGenInfo(String tableName, String dsName) {
		// 获取表的基本信息
		DatabaseTableDO databaseTable = databaseTableService.findByTableName(tableName, dsName);
		// 获取表的列信息
		List<DatabaseColumnDO> databaseColumns = databaseTableService.findColumnByTableName(tableName, dsName);

		Map<String, Object> map = new HashMap<>(16);

		// 数据库字段转实体
		List<CodeGenColumnDO> columns = databaseColumns.stream()
			.map(databaseColumn -> new CodeGenColumnDO()
				// 备注
				.setComments(databaseColumn.getColumnComment())
				// 数据库字段类型转java数据类型
				.setAttrType(CodeGenColumnTypeEnum.convertJavaType(databaseColumn.getDataType()))
				// 数据库字段类型转ts数据类型
				.setTsType(CodeGenColumnTypeEnum.convertTsType(databaseColumn.getDataType()))
				// 驼峰
				.setName(NamingCase.toCamelCase(databaseColumn.getColumnName())))
			.collect(Collectors.toList());

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
	 * 生成代码
	 * @param tableName 表名
	 */
	@SneakyThrows
	public ResponseEntity<byte[]> genCodeZip(String tableName, String dsName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		// 将代码放入压缩包内
		for (CodeGenPreviewDTO codeGenPreviewDTO : this.codeGenPreview(tableName, dsName)) {
			// 添加到zip
			CodeGenTemplateVmEnum vmEnum = CodeGenTemplateVmEnum.findByName(codeGenPreviewDTO.getName());
			String fileName = tableToJava(tableName) + vmEnum.getFileSuffixName();
			// js后缀特殊处理
			if (vmEnum.getFileSuffixName().equals(".js") || vmEnum.getFileSuffixName().equals(".ts")) {
				fileName = StrUtil.lowerFirst(tableToJava(fileName));
			}
			zip.putNextEntry(new ZipEntry(fileName));
			IOUtils.write(codeGenPreviewDTO.getContent(), zip, CharsetUtil.UTF_8);
			zip.flush();
			zip.closeEntry();
		}
		IoUtil.close(zip);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment",
				new String((tableName + ".zip").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
		return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
	}

	/**
	 * 表名转换成Java类名 大驼峰
	 */
	public static String tableToJava(String tableName) {
		// 自动去除表前缀
		return NamingCase.toPascalCase(tableName);
	}

}
