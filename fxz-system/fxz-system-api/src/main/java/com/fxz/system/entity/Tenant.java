package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.core.enums.GlobalStatusEnum;
import com.fxz.common.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 16:13
 */
@TableName("sys_tenant")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Tenant extends BaseEntity {

	/**
	 * 租户id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

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
	 * 删除标记
	 */
	@TableLogic
	private String delFlag;

	/**
	 * 系统套餐id
	 */
	public static final Long PACKAGE_ID_SYSTEM = 0L;

}
