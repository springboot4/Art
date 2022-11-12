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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.system.entity.Tenant;
import com.fxz.system.param.TenantParam;
import com.fxz.system.vo.TenantVO;

import java.util.List;

/**
 * 租户表
 *
 * @author fxz
 * @date 2022-10-01
 */
public interface TenantService extends IService<Tenant> {

	/**
	 * 校验租户信息是否合法
	 * @param id 租户id
	 */
	void validTenant(Long id);

	/**
	 * 获取所有租户id集合
	 * @return 所有租户id集合
	 */
	List<Long> getTenantIds();

	/**
	 * 根据id查询租户信息
	 * @param id 租户id
	 * @return 租户信息
	 */
	Tenant findById(Long id);

	/**
	 * 根据name查询租户Id
	 */
	Long findTenantIdById(String name);

	/**
	 * 保存租户信息
	 * @param tenant 租户视图信息
	 */
	Boolean addSysTenant(TenantVO tenant);

	/**
	 * 修改
	 */
	Boolean updateSysTenant(Tenant tenant);

	/**
	 * 分页查询租户信息
	 */
	IPage<Tenant> pageSysTenant(Page pageParam, TenantParam param);

	/**
	 * 获取全部
	 */
	List<Tenant> findAll();

	/**
	 * 删除
	 */
	Boolean deleteSysTenant(Long id);

	/**
	 * 获取指定套餐下的所有租户
	 * @param packageId 套餐id
	 * @return 指定套餐下的所有租户
	 */
	List<Tenant> getTenantListByPackageId(Long packageId);

	/**
	 * 更新指定租户的角色菜单信息
	 * @param id 租户id
	 * @param menus 菜单信息
	 */
	void updateTenantRoleMenu(Long id, List<String> menus);

	/**
	 * 校验租户下账号数量
	 */
	void validCount();

}