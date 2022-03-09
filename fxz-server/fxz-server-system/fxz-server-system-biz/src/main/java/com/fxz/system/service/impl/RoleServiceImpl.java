package com.fxz.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.core.param.PageParam;
import com.fxz.system.entity.Role;
import com.fxz.system.entity.RoleMenu;
import com.fxz.system.entity.UserRole;
import com.fxz.system.mapper.RoleMapper;
import com.fxz.system.service.IRoleMenuService;
import com.fxz.system.service.IRoleService;
import com.fxz.system.service.IUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 17:52
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

	private final IRoleMenuService roleMenuService;

	private final IUserRoleService userRoleService;

	/**
	 * 分页查询角色信息
	 */
	@Override
	public IPage<?> PageRole(PageParam pageParam, String roleName) {
		Page<Role> rolePage = new Page<>(pageParam.getCurrent(), pageParam.getSize());
		return this.getBaseMapper().selectPage(rolePage,
				Wrappers.<Role>lambdaQuery().like(StringUtils.isNotEmpty(roleName), Role::getRoleName, roleName));
	}

	/**
	 * 添加角色信息
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean addRole(Role role) {
		role.setCreateTime(new Date());
		role.setModifyTime(new Date());
		// 保存角色信息
		this.getBaseMapper().insert(role);

		// 保存角色菜单
		saveRoleMenu(role);

		return Boolean.TRUE;
	}

	/**
	 * 根据id获取角色信息
	 */
	@Override
	public Role getRoleById(Long id) {
		return this.getBaseMapper().getRoleById(id);
	}

	/**
	 * 修改角色信息
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean editRole(Role role) {
		this.getBaseMapper().updateById(role);

		// 删除角色原有菜单
		roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, role.getRoleId()));

		// 保存角色菜单
		saveRoleMenu(role);

		return null;
	}

	/**
	 * 删除角色信息
	 */
	@Override
	public Boolean deleteRoleById(Long id) {
		// 删除角色菜单关联信息
		roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, id));
		// 删除角色用户关联信息
		userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, id));
		// 删除角色信息
		return this.getBaseMapper().deleteById(id) > 0;
	}

	/**
	 * 保存角色菜单
	 */
	private void saveRoleMenu(Role role) {
		String menuId = role.getMenuId();
		if (StringUtils.isNotEmpty(menuId)) {
			String[] menuIds = menuId.split(StringPool.COMMA);
			List<RoleMenu> roleMenuList = Arrays.stream(menuIds).map(item -> {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setRoleId(role.getRoleId());
				roleMenu.setMenuId(Long.valueOf(item));
				return roleMenu;
			}).collect(Collectors.toList());
			roleMenuService.saveBatch(roleMenuList);
		}
	}

}
