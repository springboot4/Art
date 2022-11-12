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

package com.fxz.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.core.entity.router.VueRouter;
import com.fxz.common.core.exception.FxzException;
import com.fxz.common.core.util.TreeUtil;
import com.fxz.common.redis.constant.CacheConstants;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.common.security.util.SecurityUtil;
import com.fxz.system.entity.Menu;
import com.fxz.system.mapper.MenuMapper;
import com.fxz.system.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-29 18:35
 */
@Slf4j
@Service("menuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

	@Override
	public Set<String> findUserPermissions(String username) {
		List<Menu> userPermissions = this.baseMapper.findUserPermissions(username);
		return userPermissions.stream().map(Menu::getPerms).collect(Collectors.toSet());
	}

	@Override
	public List<VueRouter<Menu>> getUserRouters(String username) {
		// 指定用户名角色下的所有菜单
		List<Menu> menus = this.baseMapper.findUserMenus(username);

		// 根据菜单构建VueRouter
		List<VueRouter<Menu>> routes = menus.stream().map(Menu::toVueRouter).collect(Collectors.toList());

		// 构建树形VueRouter
		return TreeUtil.buildVueRouter(routes);
	}

	/**
	 * 获取全部的树形菜单信息(包括按钮)
	 * @return 树形菜单信息
	 */
	@Cacheable(value = CacheConstants.GLOBALLY + "menu", unless = "#result==null")
	@Override
	public List<VueRouter<Menu>> getAllMenuTree() {
		return this.baseMapper.getMenuByPid(0L);
	}

	/**
	 * 获取菜单下拉框
	 * @return 树形菜单下拉框
	 */
	@Override
	public List<VueRouter<Menu>> getTreeSelect() {
		FxzAuthUser user = SecurityUtil.getUser();
		if (Objects.isNull(user)) {
			throw new FxzException("用户未登录！");
		}

		List<VueRouter<Menu>> allMenuTree = this.getUserRouters(user.getUsername());

		VueRouter<Menu> router = new VueRouter<Menu>().setId("0").setTitle("顶级菜单").setChildren(allMenuTree);

		List<VueRouter<Menu>> result = new ArrayList<>();
		result.add(router);

		return result;
	}

	/**
	 * 保存路由信息
	 * @param vueRouter
	 */
	@Override
	public void saveMenu(VueRouter vueRouter) {
		Menu menu = new Menu();

		menu.setHidden(vueRouter.getHidden());
		menu.setParentId(Convert.toLong(vueRouter.getParentId()));
		menu.setPerms(vueRouter.getPerms());
		menu.setTitle(vueRouter.getTitle());
		menu.setKeepAlive(Convert.toInt(vueRouter.getKeepAlive()));
		menu.setType(vueRouter.getType());
		menu.setName(vueRouter.getName());
		menu.setComponent(vueRouter.getComponent());
		menu.setPath(vueRouter.getPath());
		menu.setIcon(vueRouter.getIcon());
		menu.setRedirect(vueRouter.getRedirect());
		menu.setOrderNum(Convert.toInt(vueRouter.getOrderNum()));
		menu.setApplication(vueRouter.getApplication());

		this.baseMapper.insert(menu);
	}

	/**
	 * 根据id查询路由信息
	 * @param id
	 * @return
	 */
	@Cacheable(value = CacheConstants.GLOBALLY + "menu", key = "#id", unless = "#result==null")
	@Override
	public VueRouter getMenuById(Long id) {
		Menu menu = this.getById(id);
		return menu.toVueRouter();
	}

	/**
	 * 更新路由
	 */
	@CacheEvict(value = CacheConstants.GLOBALLY + "menu", key = "#vueRouter.id")
	@Override
	public void updateMenu(VueRouter vueRouter) {
		Menu menu = new Menu();
		BeanUtil.copyProperties(vueRouter, menu);
		this.updateById(menu);
		this.update(Wrappers.<Menu>lambdaUpdate().eq(Menu::getParentId, menu.getId()).set(Menu::getApplication,
				menu.getApplication()));
	}

}
