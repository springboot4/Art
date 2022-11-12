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

package com.fxz.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.common.core.entity.router.VueRouter;
import com.fxz.system.entity.Menu;

import java.util.List;
import java.util.Set;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-29 18:34
 */
public interface IMenuService extends IService<Menu> {

	/**
	 * 通过用户名查询用户权限信息
	 * @param username 用户名
	 * @return 权限信息
	 */
	Set<String> findUserPermissions(String username);

	/**
	 * 通过用户名创建对应的 Vue路由信息
	 * @param username 用户名
	 * @return 路由信息
	 */
	List<VueRouter<Menu>> getUserRouters(String username);

	/**
	 * 获取全部的树形菜单信息(包括按钮)
	 * @return 树形菜单信息
	 */
	List<VueRouter<Menu>> getAllMenuTree();

	/**
	 * 获取菜单下拉框
	 * @return 树形菜单下拉框
	 */
	List<VueRouter<Menu>> getTreeSelect();

	/**
	 * 保存路由信息
	 * @param vueRouter
	 */
	void saveMenu(VueRouter vueRouter);

	/**
	 * 根据id查询路由信息
	 * @param id
	 * @return
	 */
	VueRouter getMenuById(Long id);

	/**
	 * 更新路由
	 */
	void updateMenu(VueRouter vueRouter);

}
