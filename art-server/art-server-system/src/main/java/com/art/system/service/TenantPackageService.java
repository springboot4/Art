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

package com.art.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.art.system.entity.TenantPackage;
import com.art.system.param.TenantPackageParam;

import java.util.List;

/**
 * 租户套餐表
 *
 * @author fxz
 * @date 2022-10-01
 */
public interface TenantPackageService extends IService<TenantPackage> {

	/**
	 * 添加
	 */
	Boolean addTenantPackage(TenantPackage tenantPackage);

	/**
	 * 更新租户套餐信息
	 */
	Boolean updateTenantPackage(TenantPackage tenantPackage);

	/**
	 * 分页查询租户套餐信息
	 */
	IPage<TenantPackage> pageTenantPackage(Page<TenantPackage> pageParam, TenantPackageParam param);

	/**
	 * 获取单条
	 */
	TenantPackage findById(Long id);

	/**
	 * 获取全部
	 */
	List<TenantPackage> findAll();

	/**
	 * 删除
	 */
	Boolean deleteTenantPackage(Long id);

	/**
	 * 校验套餐信息
	 * @param packageId 套餐id
	 * @return 套餐信息
	 */
	TenantPackage validTenantPackage(Long packageId);

}