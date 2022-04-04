package com.fxz.gen.enums;

import com.fxz.common.core.exception.FxzException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 16:24
 */
@Getter
@AllArgsConstructor
public enum CodeGenTemplateVmEnum {

	/**
	 * 实体类
	 */
	ENTITY("entity", "codegen/template/java/entity.java.vm", ".java"),
	/**
	 * mapper
	 */
	MAPPER("mapper", "codegen/template/java/mapper.java.vm", "Mapper.java"),
	/**
	 * serviceImpl
	 */
	SERVICE("service", "codegen/template/java/service.java.vm", "Service.java"),
	/**
	 * serviceImpl
	 */
	SERVICEIMPL("serviceImpl", "codegen/template/java/serviceImpl.java.vm", "ServiceImpl.java"),
	/**
	 * controller
	 */
	CONTROLLER("controller", "codegen/template/java/controller.java.vm", "Controller.java"),
	/**
	 * dto
	 */
	DTO("dto", "codegen/template/java/dto.java.vm", "Dto.java"),
	/**
	 * param
	 *//*
		 * PARAM("param", "codegen/template/java/param.java.vm", "Param.java"),
		 */
	/**
	 * api_vue2
	 */
	API_V2("api_v2", "codegen/template/vue2/api.js.vm", ".js"),
	/**
	 * list_vue2
	 */
	LIST_V2("list_v2", "codegen/template/vue2/list.vue.vm", "List.vue"),
	/**
	 * template_vue2
	 */
	TEMPLATE_V2("template_v2", "codegen/template/vue2/template.js.vm", "template.js"),
	/**
	 * edit_vue3
	 */
	EDIT_V2("edit_v2", "codegen/template/vue2/edit.vue.vm", "Edit.vue");

	/**
	 * 名称
	 */
	private final String name;

	/**
	 * 模板地址
	 */
	private final String path;

	/**
	 * 文件名
	 */
	private final String fileSuffixName;

	@SneakyThrows
	public static CodeGenTemplateVmEnum findByName(String name) {
		return Arrays.stream(CodeGenTemplateVmEnum.values()).filter(e -> Objects.equals(name, e.getName())).findFirst()
				.orElseThrow(() -> new FxzException("不支持的模板类型"));
	}

}
