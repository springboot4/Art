package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.core.enums.RoleAdminEnum;
import com.fxz.common.mp.base.BaseCreateEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 17:48
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_role")
public class Role extends BaseCreateEntity implements Serializable {

	private static final long serialVersionUID = -1189610678764394766L;

	/**
	 * 角色id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long roleId;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色code {@link RoleAdminEnum#getType()}
	 */
	private String code;

	/**
	 * 角色描述
	 */
	private String remark;

	/**
	 * 数据权限范围
	 */
	private Integer dataScope;

	/**
	 * 数据范围(指定部门数组)
	 */
	private String dataScopeDeptIds;

	/**
	 * 租户id
	 */
	private Long tenantId;

	/**
	 * 角色拥有的菜单id
	 */
	@TableField(exist = false)
	private String menuId;

}
