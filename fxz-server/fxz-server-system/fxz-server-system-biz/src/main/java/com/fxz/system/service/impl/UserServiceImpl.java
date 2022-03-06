package com.fxz.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.core.entity.PageParam;
import com.fxz.system.entity.SystemUser;
import com.fxz.system.entity.UserRole;
import com.fxz.system.mapper.UserMapper;
import com.fxz.system.param.UserInfoParam;
import com.fxz.system.service.IUserRoleService;
import com.fxz.system.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author fxz
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, SystemUser> implements IUserService {

	private final IUserRoleService userRoleService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public IPage<SystemUser> findUserDetail(UserInfoParam user, PageParam pageParam) {
		Page<SystemUser> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
		return this.baseMapper.findUserDetailPage(page, user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createUser(SystemUser user) {
		// 创建用户
		user.setCreateTime(new Date());
		user.setAvatar(SystemUser.DEFAULT_AVATAR);
		user.setPassword(passwordEncoder.encode(SystemUser.DEFAULT_PASSWORD));
		save(user);
		// 保存用户角色
		String[] roles = user.getRoleId().split(StringPool.COMMA);
		setUserRoles(user, roles);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUser(SystemUser user) {
		// 更新用户
		user.setPassword(null);
		user.setUsername(null);
		user.setCreateTime(null);
		user.setModifyTime(new Date());
		updateById(user);

		userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));
		if (StringUtils.isNotEmpty(user.getRoleId())) {
			String[] roles = user.getRoleId().split(StringPool.COMMA);
			setUserRoles(user, roles);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUsers(String[] userIds) {
		List<String> list = Arrays.asList(userIds);
		removeByIds(list);
		// 删除用户角色
		this.userRoleService.deleteUserRolesByUserId(userIds);
	}

	/**
	 * 根据用户id获取用户信息
	 */
	@Override
	public SystemUser getUserById(Long id) {
		return this.baseMapper.getUserById(id);
	}

	private void setUserRoles(SystemUser user, String[] roles) {
		Arrays.stream(roles).forEach(roleId -> {
			UserRole ur = new UserRole();
			ur.setUserId(user.getUserId());
			ur.setRoleId(Long.valueOf(roleId));
			userRoleService.save(ur);
		});
	}

	/**
	 * 通过用户名查找用户信息
	 */
	@Override
	public SystemUser findByName(String username) {
		return this.baseMapper.findByName(username);
	}

}