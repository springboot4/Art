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

package com.art.system.manager;

import cn.hutool.core.convert.Convert;
import com.art.common.core.model.VueRouter;
import com.art.system.api.dict.dto.MenuDTO;
import com.art.system.core.convert.MenuConvert;
import com.art.system.dao.dataobject.MenuDO;
import com.art.system.dao.mysql.MenuMapper;
import com.art.system.dao.redis.menu.MenuRedisKeyConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 19:25
 */
@Component
@RequiredArgsConstructor
public class MenuManager {

	private final MenuMapper menuMapper;

	public List<MenuDO> findUserMenus(String username) {
		return menuMapper.findUserMenus(username);
	}

	public List<VueRouter<MenuDO>> getMenuByPid(long pId) {
		return menuMapper.getMenuByPid(pId);
	}

	public void saveMenu(VueRouter vueRouter) {
		MenuDO menuDO = new MenuDO();

		menuDO.setHidden(vueRouter.getHidden());
		menuDO.setParentId(Convert.toLong(vueRouter.getParentId()));
		menuDO.setPerms(vueRouter.getPerms());
		menuDO.setTitle(vueRouter.getTitle());
		menuDO.setKeepAlive(Convert.toInt(vueRouter.getKeepAlive()));
		menuDO.setType(vueRouter.getType());
		menuDO.setName(vueRouter.getName());
		menuDO.setComponent(vueRouter.getComponent());
		menuDO.setPath(vueRouter.getPath());
		menuDO.setIcon(vueRouter.getIcon());
		menuDO.setRedirect(vueRouter.getRedirect());
		menuDO.setOrderNum(Convert.toInt(vueRouter.getOrderNum()));
		menuDO.setApplication(vueRouter.getApplication());

		menuMapper.insert(menuDO);
	}

	public void deleteMenuById(Long id) {
		menuMapper.deleteById(id);
	}

	public MenuDO getMenuById(Long id) {
		return menuMapper.selectById(id);
	}

	public void updateMenuById(MenuDO menuDO) {
		menuMapper.updateById(menuDO);
	}

	public void updateMenuAppByPId(Long id, Long app) {
		MenuDO menuDO = new MenuDO().setApplication(app);
		menuMapper.update(menuDO, Wrappers.<MenuDO>lambdaUpdate().eq(MenuDO::getParentId, id));
	}

	@Cacheable(value = MenuRedisKeyConstants.USER, key = "#username", unless = "#result == null")
	public List<MenuDTO> findUserPermissions(String username) {
		return MenuConvert.INSTANCE.convertList(menuMapper.findUserPermissions(username));
	}

	public List<MenuDO> getMenuByIds(List<Long> menuIds) {
		LambdaQueryWrapper<MenuDO> wrapper = Wrappers.<MenuDO>lambdaQuery().in(MenuDO::getId, menuIds);
		return menuMapper.selectList(wrapper);
	}

}
