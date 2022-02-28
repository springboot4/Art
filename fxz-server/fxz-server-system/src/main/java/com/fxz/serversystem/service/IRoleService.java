package com.fxz.serversystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.common.core.entity.PageParam;
import com.fxz.common.core.entity.system.Role;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 17:47
 */
public interface IRoleService extends IService<Role> {

	/**
	 * 分页查询角色信息
	 */
	IPage<?> PageRole(PageParam pageParam, String roleName);

	/**
	 * 添加角色信息
	 */
	Boolean addRole(Role role);

	/**
	 * 根据id获取角色信息
	 */
	Role getRoleById(Long id);

	/**
	 * 修改角色信息
	 */
	Boolean editRole(Role role);

	/**
	 * 删除角色信息
	 */
	Boolean deleteRoleById(Long id);

}
