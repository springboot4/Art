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

import com.art.common.dataPermission.annotation.DataPermission;
import com.art.system.api.user.dto.SystemUserDTO;
import com.art.system.api.user.dto.SystemUserPageDTO;
import com.art.system.api.user.dto.UserInfo;
import com.art.system.core.convert.UserConvert;
import com.art.system.dao.dataobject.*;
import com.art.system.dao.redis.user.UserRedisConstants;
import com.art.system.manager.*;
import com.art.system.service.TenantService;
import com.art.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author fxz
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

	@Resource
	private TenantService tenantService;

	private final UserPostManager userPostManager;

	private final UserRoleManager userRoleManager;

	private final RoleMenuManager roleMenuManager;

	private final MenuManager menuManager;

	private final UserManager userManager;

	private final PasswordEncoder passwordEncoder;

	@Override
	public IPage<SystemUserDTO> pageUser(SystemUserPageDTO userPageDTO) {
		return userManager.pageUser(userPageDTO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SystemUserDTO createUser(SystemUserDTO user) {
		// 校验租户账号额度
		tenantService.validCount();

		// 设置用户默认头像
		user.setAvatar(SystemUserDO.DEFAULT_AVATAR);

		// 设置用户密码
		user.setPassword((Objects.isNull(user.getPassword()) ? passwordEncoder.encode(SystemUserDO.DEFAULT_PASSWORD)
				: passwordEncoder.encode(user.getPassword())));

		// 保存用户信息
		Long userId = userManager.addUser(user);
		user.setUserId(userId);

		// 保存用户角色
		setUserRoles(user);

		// 保存用户岗位
		setUserPosts(user);

		return user;
	}

	@CacheEvict(value = UserRedisConstants.USER_INFO, key = "#user.userId")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUser(SystemUserDTO user) {
		// 更新用户基础信息
		updateUserInfo(user);

		// todo 判断不应根据头像来判断
		if (StringUtils.isBlank(user.getAvatar())) {
			// 根据用户id删除用户角色信息
			userRoleManager.deleteUserRolesByUserIds(Collections.singletonList(user.getUserId().toString()));
			// 根据用户id删除用户岗位信息
			userPostManager.deleteUserPostByUserId(user.getUserId());

			// 保存角色信息
			setUserRoles(user);

			// 保存岗位信息
			setUserPosts(user);
		}
	}

	@CacheEvict(value = UserRedisConstants.USER_INFO, key = "#user.userId")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserInfo(SystemUserDTO user) {
		// 更新用户
		if (StringUtils.isNotBlank(user.getPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		else {
			user.setPassword(null);
		}

		user.setUsername(null);
		userManager.updateUserById(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUsers(String[] userIds) {
		// 删除用户
		userManager.deleteUserByIds(Arrays.asList(userIds));

		// 删除用户角色
		userRoleManager.deleteUserRolesByUserIds(Arrays.asList(userIds));
	}

	/**
	 * 根据用户id获取用户信息
	 */
	@Cacheable(value = UserRedisConstants.USER_INFO, key = "#id", unless = "#result==null")
	@Override
	public SystemUserDTO getUserById(Long id) {
		return UserConvert.INSTANCE.convert(userManager.getUserById(id));
	}

	/**
	 * 通过用户名查找用户信息
	 */
	@DataPermission(enable = false)
	@Override
	public SystemUserDTO findByName(String username) {
		return UserConvert.INSTANCE.convert(userManager.getUserByName(username));
	}

	/**
	 * 获取用户全部信息
	 */
	@Override
	public UserInfo findUserInfo(SystemUserDTO userDTO) {
		UserInfo userInfo = new UserInfo();

		// 设置用户信息
		userInfo.setSysUser(userDTO);

		// 查询用户角色信息
		List<UserRoleDO> userRoleDOList = userRoleManager.getUserRoleByUserId(userDTO.getUserId());
		if (CollectionUtils.isEmpty(userRoleDOList)) {
			return userInfo;
		}

		// 查询角色菜单信息
		List<Long> roleIds = userRoleDOList.stream().map(UserRoleDO::getRoleId).collect(Collectors.toList());
		List<RoleMenuDO> roleMenuDOList = roleMenuManager.getRoleMenuByRoleIds(roleIds);
		if (CollectionUtils.isEmpty(roleMenuDOList)) {
			return userInfo;
		}

		// 查询菜单信息
		List<Long> menuIds = roleMenuDOList.stream().map(RoleMenuDO::getMenuId).collect(Collectors.toList());
		List<MenuDO> menuDOList = menuManager.getMenuByIds(menuIds);
		if (CollectionUtils.isEmpty(menuDOList)) {
			return userInfo;
		}

		// 设置用户权限标识
		List<String> permissions = menuDOList.stream().map(MenuDO::getPerms).distinct().collect(Collectors.toList());
		userInfo.setPermissions(permissions);

		return userInfo;
	}

	/**
	 * 通过手机号查找用户信息
	 * @param mobile 手机号
	 * @return 用户信息
	 */
	@Override
	public SystemUserDTO findByMobile(String mobile) {
		return UserConvert.INSTANCE.convert(userManager.getUserByMobile(mobile));
	}

	/**
	 * 获取当前租户下的所有用户数
	 * @return 当前租户下的所有用户数
	 */
	@Override
	public long count() {
		return userManager.count();
	}

	/**
	 * 保存用户的岗位信息
	 * @param user 用户信息
	 */
	private void setUserPosts(SystemUserDTO user) {
		if (StringUtils.isBlank(user.getPostId())) {
			return;
		}

		List<UserPostDO> list = Arrays.stream(user.getPostId().split(StringPool.COMMA)).map(postId -> {
			UserPostDO up = new UserPostDO();
			up.setUserId(user.getUserId());
			up.setPostId(Long.valueOf(postId));
			return up;
		}).collect(Collectors.toList());

		userPostManager.addUserPosts(list);
	}

	/**
	 * 保存用户的角色信息
	 * @param user 用户信息
	 */
	private void setUserRoles(SystemUserDTO user) {
		if (StringUtils.isBlank(user.getRoleId())) {
			return;
		}

		List<UserRoleDO> list = Arrays.stream(user.getRoleId().split(StringPool.COMMA)).map(roleId -> {
			UserRoleDO ur = new UserRoleDO();
			ur.setUserId(user.getUserId());
			ur.setRoleId(Long.valueOf(roleId));
			return ur;
		}).collect(Collectors.toList());

		userRoleManager.addUserRoles(list);
	}

}