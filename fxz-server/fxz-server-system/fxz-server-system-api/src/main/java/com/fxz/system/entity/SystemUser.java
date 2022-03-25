package com.fxz.system.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fxz.common.core.annotation.IsMobile;
import com.fxz.common.mp.base.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 15:22
 */
@Data
@TableName("t_user")
public class SystemUser extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 3803544824764133607L;

	/**
	 * 用户状态：有效
	 */
	public static final String STATUS_VALID = "1";

	/**
	 * 用户状态：锁定
	 */
	public static final String STATUS_LOCK = "0";

	/**
	 * 默认头像
	 */
	public static final String DEFAULT_AVATAR = "https://preview.pro.antdv.com/avatar2.jpg";

	/**
	 * 默认密码
	 */
	public static final String DEFAULT_PASSWORD = "1234qwer";

	/**
	 * 性别男
	 */
	public static final String SEX_MALE = "0";

	/**
	 * 性别女
	 */
	public static final String SEX_FEMALE = "1";

	/**
	 * 性别保密
	 */
	public static final String SEX_UNKNOW = "2";

	/**
	 * 用户 ID
	 */
	@TableId(value = "USER_ID", type = IdType.AUTO)
	private Long userId;

	/**
	 * 用户名
	 */
	@Size(min = 4, max = 10, message = "{range}")
	@TableField("USERNAME")
	private String username;

	/**
	 * 密码
	 */
	@TableField("PASSWORD")
	private String password;

	/**
	 * 部门 ID
	 */
	@TableField("DEPT_ID")
	private Long deptId;

	/**
	 * 邮箱
	 */
	@Email(message = "{email}")
	@TableField("EMAIL")
	private String email;

	/**
	 * 联系电话
	 */
	@IsMobile(message = "{mobile}")
	@TableField("MOBILE")
	private String mobile;

	/**
	 * 状态 0锁定 1有效
	 */
	@NotBlank(message = "{required}")
	@TableField("STATUS")
	private String status;

	/**
	 * 最近访问时间
	 */
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
	@TableField("LAST_LOGIN_TIME")
	private Date lastLoginTime;

	/**
	 * 性别 0男 1女 2 保密
	 */
	@NotBlank(message = "{required}")
	@TableField("SSEX")
	private String sex;

	/**
	 * 头像
	 */
	@TableField("AVATAR")
	private String avatar;

	/**
	 * 描述
	 */
	@Size(max = 100, message = "{noMoreThan}")
	@TableField("DESCRIPTION")
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
	 * 角色 ID
	 */
	@TableField(exist = false)
	private String roleId;

	@TableField(exist = false)
	private String roleName;

}
