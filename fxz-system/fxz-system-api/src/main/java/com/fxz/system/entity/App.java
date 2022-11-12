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

package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.core.validator.ValidationGroup;
import com.fxz.common.mp.base.BaseIdEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 系统应用表
 *
 * @author fxz
 * @date 2022-09-12
 */
@Schema(title = "系统应用")
@Data
@TableName("sys_app")
public class App extends BaseIdEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 应用名称
	 */
	@Schema(description = "应用名称")
	@Size(min = 1, max = 10, message = "应用名称长度在1-10之间!", groups = ValidationGroup.add.class)
	@NotNull(message = "应用名称不能为空!", groups = ValidationGroup.add.class)
	private String name;

	/**
	 * 应用编码
	 */
	@Schema(description = "应用编码")
	@Size(min = 1, max = 10, message = "应用编码长度在1-10之间!", groups = ValidationGroup.add.class)
	@NotNull(message = "应用编码不能为空!", groups = ValidationGroup.add.class)
	private String code;

	/**
	 * 图标
	 */
	@Schema(description = "图标")
	@NotNull(message = "图标不能为空!", groups = ValidationGroup.update.class)
	private String icon;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	@NotNull(message = "排序不能为空!", groups = ValidationGroup.update.class)
	private Integer sort;

}
