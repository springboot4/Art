package com.fxz.gen.service;

import com.fxz.gen.dto.CodeGenPreview;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 15:40
 */
public interface CodeGeneratorService {

	/**
	 * 生成预览代码
	 * @param tableName 表名
	 * @return 预览代码
	 */
	List<CodeGenPreview> codeGenPreview(String tableName, String dsName);

}
