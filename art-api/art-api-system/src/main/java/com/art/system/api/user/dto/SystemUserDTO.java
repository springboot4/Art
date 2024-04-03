/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.system.api.user.dto;

import cn.hutool.core.date.DatePattern;
import com.art.common.sensitive.core.annotation.fixed.MobileSensitive;
import com.art.common.sensitive.core.annotation.regex.EmailSensitive;
import com.art.core.common.annotation.CheckMobileValid;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/26 15:51
 */
@Accessors(chain = true)
@Data
public class SystemUserDTO implements Serializable {

	private static final long serialVersionUID = -1L;

	/**
	 * 用户状态：有效
	 */
	public static final String STATUS_VALID = "1";

	/**
	 * 默认头像
	 */
	public static final String DEFAULT_AVATAR = "";

	/**
	 * 默认密码
	 */
	public static final String DEFAULT_PASSWORD = "123456";

	/**
	 * 用户 ID
	 */
	private Long userId;

	/**
	 * 租户 ID
	 */
	private Long tenantId;

	/**
	 * 用户名
	 */
	@Size(min = 3, max = 10, message = "{range}")
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 部门 ID
	 */
	private Long deptId;

	/**
	 * 邮箱
	 */
	@EmailSensitive
	@Email(message = "{email}")
	private String email;

	/**
	 * 联系电话
	 */
	@MobileSensitive
	@CheckMobileValid(message = "{mobile}")
	private String mobile;

	/**
	 * 状态 0锁定 1有效
	 */
	@NotBlank(message = "{required}")
	private String status;

	/**
	 * 最近访问时间
	 */
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Date lastLoginTime;

	/**
	 * 性别 0男 1女 2 保密
	 */
	@NotBlank(message = "{required}")
	private String sex;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 描述
	 */
	@Size(max = 100, message = "{noMoreThan}")
	private String description;

	/**
	 * 部门名称
	 */
	private String deptName;

	private String createTimeFrom;

	private String createTimeTo;

	/**
	 * 岗位 ID
	 */
	private String postId;

	private String postName;

	/**
	 * 角色 ID
	 */
	private String roleId;

	private String roleName;

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

	/**
	 * 权限字符串
	 */
	private String perms;

}
