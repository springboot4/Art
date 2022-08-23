package com.fxz.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.system.entity.UserRole;
import com.fxz.system.mapper.UserRoleMapper;
import com.fxz.system.service.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fxz
 */
@Service("userRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUserRolesByRoleId(String[] roleIds) {
		this.baseMapper.deleteByRoleId(roleIds);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUserRolesByUserId(String[] userIds) {
		this.baseMapper.deleteByUserId(userIds);
	}

}