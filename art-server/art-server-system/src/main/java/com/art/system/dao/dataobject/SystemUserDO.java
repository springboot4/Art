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

package com.art.system.dao.dataobject;

import cn.hutool.core.date.DatePattern;
import com.art.common.sensitive.core.annotation.fixed.MobileSensitive;
import com.art.common.sensitive.core.annotation.regex.EmailSensitive;
import com.art.core.common.annotation.CheckMobileValid;
import com.art.mybatis.common.base.BaseCreateEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-28 15:22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@TableName("sys_user")
public class SystemUserDO extends BaseCreateEntity {

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
	@TableId(type = IdType.AUTO)
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
	@TableField(value = "ssex")
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
	@TableField(exist = false)
	private String deptName;

	@TableField(exist = false)
	private String createTimeFrom;

	@TableField(exist = false)
	private String createTimeTo;

	/**
	 * 岗位 ID
	 */
	@TableField(exist = false)
	private String postId;

	@TableField(exist = false)
	private String postName;

	/**
	 * 角色 ID
	 */
	@TableField(exist = false)
	private String roleId;

	@TableField(exist = false)
	private String roleName;

}
