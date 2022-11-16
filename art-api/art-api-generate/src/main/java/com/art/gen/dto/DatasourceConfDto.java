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

package com.art.gen.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据源表
 *
 * @author fxz
 * @date 2022-03-31
 */
@Data
public class DatasourceConfDto {

	private static final long serialVersionUID = -3090644297573721386L;

	private Long id;

	private String name;

	private String url;

	private String username;

	private String password;

	private String delFlag;

	private LocalDateTime createTime;

	private String createBy;

	private LocalDateTime updateTime;

	private String updateBy;

}