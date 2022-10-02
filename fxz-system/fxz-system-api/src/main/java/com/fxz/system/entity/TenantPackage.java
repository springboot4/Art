package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 16:22
 */
@Data
@TableName("sys_tenant_package")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class TenantPackage extends BaseEntity {

	/**
	 * 套餐id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 套餐名
	 */
	private String name;

	/**
	 * 套餐状态
	 * <p>
	 * 枚举 {@link com.fxz.common.core.enums.GlobalStatusEnum}
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 套餐关联的菜单编号
	 */
	private String menuIds;

	/**
	 * 删除标记
	 */
	@TableLogic
	private String delFlag;

}
