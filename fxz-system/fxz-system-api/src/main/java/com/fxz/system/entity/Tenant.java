package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.core.enums.GlobalStatusEnum;
import com.fxz.common.mp.base.MpEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 16:13
 */
@TableName("sys_tenant")
@Data
public class Tenant extends MpEntity {

	/**
	 * 租户名
	 */
	private String name;

	/**
	 * 当前租户管理员id
	 */
	private Long tenantAdminId;

	/**
	 * 当前租户管理员姓名
	 */
	private String tenantAdminName;

	/**
	 * 当前租户管理员手机号
	 */
	private String tenantAdminMobile;

	/**
	 * 租户状态
	 * <p>
	 * 枚举 {@link GlobalStatusEnum}
	 */
	private Integer status;

	/**
	 * 租户套餐id
	 */
	private Long packageId;

	/**
	 * 租户过期时间
	 */
	private LocalDateTime expireTime;

	/**
	 * 账号数量
	 */
	private Integer accountCount;

	/**
	 * 系统套餐id
	 */
	public static final Long PACKAGE_ID_SYSTEM = 0L;

}
