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

package com.fxz.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 12:25
 */
@Data
public class UserInfoDto implements Serializable {

	private static final long serialVersionUID = -6220014292300727768L;

	/**
	 * 用户 ID
	 */
	private Long userId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 联系电话
	 */
	private String mobile;

	/**
	 * 状态 0锁定 1有效
	 */
	private String status;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 性别 0男 1女 2 保密
	 */
	private String sex;

}
