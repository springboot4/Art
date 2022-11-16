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

package com.art.common.core.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询参数
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022-02-27 12:40
 */
@Data
public class PageParam implements Serializable {

	private static final long serialVersionUID = -6136931343132969615L;

	/**
	 * 当前页
	 */
	private int current = 1;

	/**
	 * 每页记录数
	 */
	private int size = 10;

}
