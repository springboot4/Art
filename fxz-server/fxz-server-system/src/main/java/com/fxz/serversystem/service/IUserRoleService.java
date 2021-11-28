package com.fxz.serversystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.entity.system.UserRole;

/**
 * @author fxz
 */
public interface IUserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String[] roleIds);

	void deleteUserRolesByUserId(String[] userIds);
}