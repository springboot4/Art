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

package com.art.system.api.log.dto;

import com.art.common.core.model.BasePageEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 20:56
 */
@Data
public class OperLogPageDTO extends BasePageEntity implements Serializable {

	private static final long serialVersionUID = -1L;

	private Long id;

	private String title;

	private Integer businessType;

	private String method;

	private String requestMethod;

	private String operName;

	private String operUrl;

	private String operIp;

	private String operParam;

	private Integer status;

	private String errorMsg;

	private Long time;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新者
	 */
	private String updateBy;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

}
