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

import com.art.common.core.exception.FxzException;
import com.art.common.core.model.VueRouter;
import com.art.common.core.util.TreeUtil;
import com.art.common.security.core.model.ArtAuthUser;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.system.api.dict.dto.MenuDTO;
import com.art.system.core.convert.MenuConvert;
import com.art.system.dao.dataobject.MenuDO;
import com.art.system.dao.dataobject.SystemUserDO;
import com.art.system.manager.AppManager;
import com.art.system.manager.MenuManager;
import com.art.system.manager.UserManager;
import com.art.system.service.MenuService;
import com.art.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-29 18:35
 */
@Slf4j
@Service("menuService")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

	private final MenuManager menuManager;

	private final AppManager appManager;

	private final UserManager userManager;

	private final RoleService roleService;

	@Override
	public Set<String> findUserPermissions(String username) {
		List<MenuDTO> userPermissions = menuManager.findUserPermissions(username);
		return userPermissions.stream().map(MenuDTO::getPerms).collect(Collectors.toSet());
	}

	@Override
	public List<VueRouter<MenuDTO>> getUserRouters(String username) {
		// 指定用户名角色下的所有菜单
		List<MenuDO> menuDOList = menuManager.findUserMenus(username);

		// 根据菜单构建VueRouter
		List<VueRouter<MenuDO>> routes = menuDOList.stream().map(MenuDO::toVueRouter).collect(Collectors.toList());

		// 构建树形VueRouter
		return MenuConvert.INSTANCE.convert(TreeUtil.buildVueRouter(routes));
	}

	/**
	 * 获取全部的树形菜单信息(包括按钮)
	 * @return 树形菜单信息
	 */
	@Override
	public List<VueRouter<MenuDTO>> getAllMenuTree() {
		return MenuConvert.INSTANCE.convert(menuManager.getMenuByPid(0L));
	}

	/**
	 * 获取菜单下拉框
	 * @return 树形菜单下拉框
	 */
	@Override
	public List<VueRouter<MenuDTO>> getTreeSelect() {
		ArtAuthUser user = Optional.ofNullable(SecurityUtil.getUser()).orElseThrow(() -> new FxzException("用户未登录！"));

		List<VueRouter<MenuDTO>> allMenuTree = this.getUserRouters(user.getUsername());

		VueRouter<MenuDTO> router = new VueRouter<MenuDTO>().setId("0").setTitle("顶级菜单").setChildren(allMenuTree);

		List<VueRouter<MenuDTO>> result = new ArrayList<>();
		result.add(router);
		return result;
	}

	/**
	 * 保存路由信息
	 * @param vueRouter
	 */
	@Override
	public void saveMenu(VueRouter vueRouter) {
		menuManager.saveMenu(vueRouter);
	}

	/**
	 * 根据id查询路由信息
	 * @param id
	 * @return
	 */
	@Override
	public VueRouter<MenuDO> getMenuById(Long id) {
		MenuDO menuDO = menuManager.getMenuById(id);
		return menuDO.toVueRouter();
	}

	/**
	 * 更新路由
	 */
	@Override
	public void updateMenu(VueRouter vueRouter) {
		MenuDO menuDO = MenuConvert.INSTANCE.convert(vueRouter);
		menuManager.updateMenuById(menuDO);
		menuManager.updateMenuAppByPId(menuDO.getId(), menuDO.getApplication());
	}

	@Override
	public Map<String, Object> getUserRoutersAndAuthority() {
		// 返回结果集
		Map<String, Object> result = new HashMap<>(4);

		ArtAuthUser user = SecurityUtil.getUser();
		Optional.ofNullable(user).map(ArtAuthUser::getUsername).ifPresent(userName -> {
			// 构建用户路由对象
			result.put("routes", getUserRouters(userName));
			// 封装用户权限信息
			result.put("permissions", user.getAuthorities().toArray());
			// 封装应用信息
			result.put("apps", appManager.listApp());
		});

		return result;
	}

	@Override
	public List<VueRouter<MenuDTO>> getUserMenuTree(Long userId) {
		SystemUserDO user = userManager.getUserById(userId);

		if (roleService.isSuperAdmin(user.getRoleId())) {
			return getAllMenuTree();
		}
		else {
			return getUserRouters(user.getUsername());
		}
	}

	@Override
	public void removeById(Long id) {
		menuManager.deleteMenuById(id);
	}

}
