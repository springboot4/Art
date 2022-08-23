package com.fxz.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.core.param.PageParam;
import com.fxz.common.redis.constant.CacheConstants;
import com.fxz.system.dto.UserInfoDto;
import com.fxz.system.entity.*;
import com.fxz.system.mapper.UserMapper;
import com.fxz.system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author fxz
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, SystemUser> implements IUserService {

	private final UserPostService userPostService;

	private final IUserRoleService userRoleService;

	private final IRoleMenuService roleMenuService;

	private final IMenuService menuService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public IPage<SystemUser> findUserDetail(UserInfoDto user, PageParam pageParam) {
		Page<SystemUser> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
		return this.baseMapper.findUserDetailPage(page, user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createUser(SystemUser user) {
		// 创建用户
		user.setAvatar(SystemUser.DEFAULT_AVATAR);
		user.setPassword("{bcrypt}" + passwordEncoder.encode(SystemUser.DEFAULT_PASSWORD));

		// 保存用户信息
		save(user);

		// 保存用户角色
		setUserRoles(user);

		// 保存用户岗位
		setUserPosts(user);
	}

	@CacheEvict(value = CacheConstants.GLOBALLY + "user", key = "#user.userId+':userInfo'")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUser(SystemUser user) {
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
			userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));
			userPostService.remove(new LambdaQueryWrapper<UserPost>().eq(UserPost::getUserId, user.getUserId()));

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
	public SystemUser getUserById(Long id) {
		return this.baseMapper.getUserById(id);
	}

	/**
	 * 通过用户名查找用户信息
	 */
	@Cacheable(value = CacheConstants.GLOBALLY + "user", key = "#username+':userInfo'")
	@Override
	public SystemUser findByName(String username) {
		return this.baseMapper.findByName(username);
	}

	/**
	 * 获取用户全部信息
	 */
	@Override
	public UserInfo findUserInfo(SystemUser systemUser) {
		UserInfo userInfo = new UserInfo();
		userInfo.setSysUser(systemUser);
		List<UserRole> userRoles = userRoleService
				.list(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, systemUser.getUserId()));
		if (CollectionUtils.isNotEmpty(userRoles)) {
			Set<Long> roleSet = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
			List<RoleMenu> roleMenus = roleMenuService
					.list(Wrappers.<RoleMenu>lambdaQuery().in(RoleMenu::getRoleId, roleSet));
			if (CollectionUtils.isNotEmpty(roleMenus)) {
				Set<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
				List<Menu> menus = menuService.list(Wrappers.<Menu>lambdaQuery().in(Menu::getId, menuIds));
				if (CollectionUtils.isNotEmpty(menus)) {
					List<String> permissions = menus.stream().map(Menu::getPerms).distinct()
							.collect(Collectors.toList());
					userInfo.setPermissions(permissions);
				}
			}
		}
		return userInfo;
	}

	/**
	 * 通过手机号查找用户信息
	 * @param mobile 手机号
	 * @return 用户信息
	 */
	@Override
	public SystemUser findByMobile(String mobile) {
		return this.baseMapper.findByMobile(mobile);
	}

	/**
	 * 保存用户的岗位信息
	 * @param user 用户信息
	 */
	private void setUserPosts(SystemUser user) {
		if (StringUtils.isBlank(user.getPostId())) {
			return;
		}

		String[] posts = user.getPostId().split(StringPool.COMMA);

		List<UserPost> list = Arrays.stream(posts).map(postId -> {
			UserPost up = new UserPost();
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
	private void setUserRoles(SystemUser user) {
		if (StringUtils.isBlank(user.getRoleId())) {
			return;
		}

		String[] roles = user.getRoleId().split(StringPool.COMMA);

		List<UserRole> list = Arrays.stream(roles).map(roleId -> {
			UserRole ur = new UserRole();
			ur.setUserId(user.getUserId());
			ur.setRoleId(Long.valueOf(roleId));
			return ur;
		}).collect(Collectors.toList());

		userRoleService.saveBatch(list);
	}

}