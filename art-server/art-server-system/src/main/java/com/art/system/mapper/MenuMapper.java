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

package com.art.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.art.common.core.entity.router.VueRouter;
import com.art.system.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-29 18:28
 */
public interface MenuMapper extends BaseMapper<Menu> {

	/**
	 * 通过用户名查询权限信息
	 * @param username 用户名称
	 * @return 权限信息
	 */
	List<Menu> findUserPermissions(String username);

	/**
	 * 通过用户名查询菜单信息
	 * @param username 用户名
	 * @return 菜单信息
	 */
	List<Menu> findUserMenus(String username);

	/**
	 * 根据父节点id查询路由信息
	 * @param id 父节点id
	 * @return 树形菜单信息
	 */
	List<VueRouter<Menu>> getMenuByPid(@Param("id") Long id);

}
