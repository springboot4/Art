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

package com.art.gen.core.enums;

import com.art.common.core.exception.FxzException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-03-03 16:24
 */
@Getter
@AllArgsConstructor
public enum CodeGenTemplateVmEnum {

	/////////////////////////////////////////// java/////////////////////////////////////////////////////////
	/**
	 * 实体类
	 */
	ENTITY("entity", "codegen/template/java/entity.java.vm", "DO.java"),

	/**
	 * manager
	 */
	MANAGER("manager", "codegen/template/java/manager.java.vm", "Manager.java"),

	/**
	 * mapper
	 */
	MAPPER("mapper", "codegen/template/java/mapper.java.vm", "Mapper.java"),

	/**
	 * service
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
	DTO("dto", "codegen/template/java/dto.java.vm", "DTO.java"),

	/**
	 * page
	 */
	PAGE("page", "codegen/template/java/page.java.vm", "PageDTO.java"),

	/**
	 * convert
	 */
	CONVERT("convert", "codegen/template/java/convert.java.vm", "Convert.java"),
	/////////////////////////////////////////// java/////////////////////////////////////////////////////////

	/////////////////////////////////////////// vue3/////////////////////////////////////////////////////////
	/**
	 * api_vue3
	 */
	API_V3("api_v3", "codegen/template/vue3/index.ts.vm", "Index.ts"),

	/**
	 * type_vue3
	 */
	TYPE_V3("type_v3", "codegen/template/vue3/types.ts.vm", "Types.ts"),

	/**
	 * list_vue3
	 */
	LIST_V3("list_v3", "codegen/template/vue3/list.vue.vm", "List.vue"),

	/**
	 * edit_vue3
	 */
	EDIT_V3("edit_v3", "codegen/template/vue3/edit.vue.vm", "Edit.vue"),
	/////////////////////////////////////////// vue3/////////////////////////////////////////////////////////

	/////////////////////////////////////////// vue2/////////////////////////////////////////////////////////
	/**
	 * api_vue2
	 */
	API_V2("api_v2", "codegen/template/vue2/api.js.vm", ".js"),

	/**
	 * list_vue2
	 */
	LIST_V2("list_v2", "codegen/template/vue2/list.vue.vm", "Vue2List.vue"),

	/**
	 * template_vue2
	 */
	TEMPLATE_V2("template_v2", "codegen/template/vue2/template.js.vm", "template.js"),

	/**
	 * edit_vue2
	 */
	EDIT_V2("edit_v2", "codegen/template/vue2/edit.vue.vm", "Vue2Edit.vue");
	/////////////////////////////////////////// vue2/////////////////////////////////////////////////////////

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
		return Arrays.stream(CodeGenTemplateVmEnum.values())
			.filter(e -> Objects.equals(name, e.getName()))
			.findFirst()
			.orElseThrow(() -> new FxzException("不支持的模板类型"));
	}

}
