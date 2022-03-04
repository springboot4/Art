package com.fxz.common.gen.controller;

import com.fxz.common.core.entity.FxzResponse;
import com.fxz.common.gen.service.impl.CodeGeneratorServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public FxzResponse codeGenPreview() {
		log.info("code预览...");
		return new FxzResponse().data(codeGeneratorService.codeGenPreview("t_user"));
	}

}
