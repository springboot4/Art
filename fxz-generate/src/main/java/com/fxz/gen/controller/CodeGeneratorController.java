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

package com.fxz.gen.controller;

import com.fxz.common.mp.result.Result;
import com.fxz.gen.dto.CodeGenPreview;
import com.fxz.gen.service.impl.CodeGeneratorServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 15:33
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/gen/code")
public class CodeGeneratorController {

	private final CodeGeneratorServiceImpl codeGeneratorService;

	/**
	 * 生成预览代码
	 * @param tableName 表名
	 * @return 预览代码
	 */
	@GetMapping("/codeGenPreview")
	public Result<List<CodeGenPreview>> codeGenPreview(@RequestParam("tableName") String tableName,
			@RequestParam("dsName") String dsName) {
		return Result.success(codeGeneratorService.codeGenPreview(tableName, dsName));
	}

	/**
	 * 生成代码
	 * @param tableName 表名
	 */
	@GetMapping("/genCodeZip")
	public ResponseEntity<byte[]> genCodeZip(@RequestParam("tableName") String tableName,
			@RequestParam("dsName") String dsName) {
		return codeGeneratorService.genCodeZip(tableName, dsName);
	}

}
