/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.art.system.service.impl;

import com.art.common.core.param.PageParam;
import com.art.common.redis.constant.CacheConstants;
import com.art.system.dao.dataobject.*;
import com.art.system.dao.mysql.UserMapper;
import com.art.system.api.user.UserInfo;
import com.art.system.api.user.UserInfoDTO;
import com.art.system.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author fxz
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, SystemUserDO> implements UserService {

	@Resource
	private TenantService tenantService;

	private final UserPostService userPostService;

	private final UserRoleService userRoleService;

	private final RoleMenuService roleMenuService;

	private final MenuService menuService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public IPage<SystemUserDO> findUserDetail(UserInfoDTO user, PageParam pageParam) {
		Page<SystemUserDO> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
		return this.baseMapper.findUserDetailPage(page, user);
	}

	@Cacheable(value = CacheConstants.GLOBALLY + "user", key = "#user.userId+':userInfo'")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public SystemUserDO createUser(SystemUserDO user) {
		// 校验租户账号额度
		tenantService.validCount();

		// 设置用户默认头像
		user.setAvatar(SystemUserDO.DEFAULT_AVATAR);

		// 设置用户密码
		user.setPassword((Objects.isNull(user.getPassword()) ? passwordEncoder.encode(SystemUserDO.DEFAULT_PASSWORD)
				: passwordEncoder.encode(user.getPassword())));

		// 保存用户信息
		save(user);

		// 保存用户角色
		setUserRoles(user);

		// 保存用户岗位
		setUserPosts(user);

		return user;
	}

	@CacheEvict(value = CacheConstants.GLOBALLY + "user", key = "#user.userId+':userInfo'")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUser(SystemUserDO user) {
		// 更新用户
		if (StringUtils.isNotBlank(user.getPassword())) {
			user.setPassword("{bcrypt}" + passwordEncoder.encode(user.getPassword()));
		}
		else {
			user.setPassword(null);
		}

		user.setUsername(null);
		updateById(user);

		if (StringUtils.isBlank(user.getAvatar())) {
			userRoleService.remove(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getUserId, user.getUserId()));
			userPostService.remove(new LambdaQueryWrapper<UserPostDO>().eq(UserPostDO::getUserId, user.getUserId()));

			// 保存角色信息
			setUserRoles(user);

			// 保存岗位信息
			setUserPosts(user);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUsers(String[] userIds) {
		// 删除用户
		removeByIds(Arrays.asList(userIds));

		// 删除用户角色
		this.userRoleService.deleteUserRolesByUserId(userIds);
	}

	/**
	 * 根据用户id获取用户信息
	 */
	@Cacheable(value = CacheConstants.GLOBALLY + "user", key = "#id+':userInfo'")
	@Override
	public SystemUserDO getUserById(Long id) {
		return this.baseMapper.getUserById(id);
	}

	/**
	 * 通过用户名查找用户信息
	 */
	@Cacheable(value = CacheConstants.GLOBALLY + "user", key = "#username+':userInfo'")
	@Override
	public SystemUserDO findByName(String username) {
		return this.baseMapper.findByName(username);
	}

	/**
	 * 获取用户全部信息
	 */
	@Override
	public UserInfo findUserInfo(SystemUserDO systemUserDO) {
		UserInfo userInfo = new UserInfo();

		// 设置用户信息
		userInfo.setSysUser(systemUserDO);

		// 查询用户角色信息
		List<UserRoleDO> userRoleDOS = userRoleService
				.list(Wrappers.<UserRoleDO>lambdaQuery().eq(UserRoleDO::getUserId, systemUserDO.getUserId()));
		if (CollectionUtils.isEmpty(userRoleDOS)) {
			return userInfo;
		}

		// 查询角色菜单信息
		List<RoleMenuDO> roleMenuDOS = roleMenuService.list(Wrappers.<RoleMenuDO>lambdaQuery().in(RoleMenuDO::getRoleId,
				userRoleDOS.stream().map(UserRoleDO::getRoleId).collect(Collectors.toSet())));
		if (CollectionUtils.isEmpty(roleMenuDOS)) {
			return userInfo;
		}

		// 查询菜单信息
		List<MenuDO> menuDOS = menuService.list(Wrappers.<MenuDO>lambdaQuery().in(MenuDO::getId,
				roleMenuDOS.stream().map(RoleMenuDO::getMenuId).collect(Collectors.toSet())));
		if (CollectionUtils.isEmpty(menuDOS)) {
			return userInfo;
		}

		// 设置用户权限标识
		List<String> permissions = menuDOS.stream().map(MenuDO::getPerms).distinct().collect(Collectors.toList());
		userInfo.setPermissions(permissions);

		return userInfo;
	}

	/**
	 * 通过手机号查找用户信息
	 * @param mobile 手机号
	 * @return 用户信息
	 */
	@Override
	public SystemUserDO findByMobile(String mobile) {
		return this.baseMapper.findByMobile(mobile);
	}

	/**
	 * 保存用户的岗位信息
	 * @param user 用户信息
	 */
	private void setUserPosts(SystemUserDO user) {
		if (StringUtils.isBlank(user.getPostId())) {
			return;
		}

		List<UserPostDO> list = Arrays.stream(user.getPostId().split(StringPool.COMMA)).map(postId -> {
			UserPostDO up = new UserPostDO();
			up.setUserId(user.getUserId());
			up.setPostId(Long.valueOf(postId));
			return up;
		}).collect(Collectors.toList());

		userPostService.saveBatch(list);
	}

	/**
	 * 保存用户的角色信息
	 * @param user 用户信息
	 */
	private void setUserRoles(SystemUserDO user) {
		if (StringUtils.isBlank(user.getRoleId())) {
			return;
		}

		List<UserRoleDO> list = Arrays.stream(user.getRoleId().split(StringPool.COMMA)).map(roleId -> {
			UserRoleDO ur = new UserRoleDO();
			ur.setUserId(user.getUserId());
			ur.setRoleId(Long.valueOf(roleId));
			return ur;
		}).collect(Collectors.toList());

		userRoleService.saveBatch(list);
	}

}