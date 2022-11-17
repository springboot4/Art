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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.art.common.core.enums.GlobalStatusEnum;
import com.art.common.core.exception.FxzException;
import com.art.system.entity.Tenant;
import com.art.system.entity.TenantPackage;
import com.art.system.mapper.TenantPackageMapper;
import com.art.system.param.TenantPackageParam;
import com.art.system.service.TenantPackageService;
import com.art.system.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 租户套餐表
 *
 * @author fxz
 * @date 2022-10-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantPackageServiceImpl extends ServiceImpl<TenantPackageMapper, TenantPackage>
		implements TenantPackageService {

	@Resource
	private TenantService tenantService;

	private final TenantPackageMapper tenantPackageMapper;

	/**
	 * 添加
	 */
	@Override
	public Boolean addTenantPackage(TenantPackage tenantPackage) {
		tenantPackageMapper.insert(tenantPackage);
		return Boolean.TRUE;
	}

	/**
	 * 更新租户套餐信息
	 */
	@Override
	public Boolean updateTenantPackage(TenantPackage tenantPackage) {
		// 校验套餐是否存在
		TenantPackage packageExists = validTenantPackageExists(tenantPackage.getId());

		// 更新套餐信息
		tenantPackageMapper.updateById(tenantPackage);

		// 租户原菜单信息
		String[] newMenus = tenantPackage.getMenuIds().split(StrPool.COMMA);
		// 更新后的菜单信息
		String[] oldMenus = packageExists.getMenuIds().split(StrPool.COMMA);

		// 菜单信息变化 则更新租户下的角色菜单信息
		if (!CollUtil.isEqualList(Arrays.asList(newMenus), Arrays.asList(oldMenus))) {
			// 本套餐下的所有租户
			List<Tenant> tenants = tenantService.getTenantListByPackageId(tenantPackage.getId());

			// 遍历所有租户 更新租户下的角色菜单信息
			tenants.forEach(t -> tenantService.updateTenantRoleMenu(t.getId(), Arrays.asList(newMenus)));
		}

		return Boolean.TRUE;
	}

	/**
	 * 删除租户套餐信息
	 */
	@Override
	public Boolean deleteTenantPackage(Long id) {
		// 校验套餐是否存在
		validTenantPackageExists(id);

		// 校验套餐是否正在使用
		validTenantPackageUsed(id);

		// 删除套餐信息
		tenantPackageMapper.deleteById(id);

		return Boolean.TRUE;
	}

	/**
	 * 校验套餐信息
	 * @param packageId 套餐id
	 * @return 套餐信息
	 */
	@Override
	public TenantPackage validTenantPackage(Long packageId) {
		TenantPackage tenantPackage = this.getById(packageId);

		if (Objects.isNull(tenantPackage)) {
			throw new FxzException("租户套餐不存在！");
		}
		else if (GlobalStatusEnum.DISABLE.getValue().equals(tenantPackage.getStatus())) {
			throw new FxzException("套餐未开启！");
		}

		return tenantPackage;
	}

	/**
	 * 校验套餐是否存在
	 * @param id 套餐id
	 * @return 租户套餐
	 */
	private TenantPackage validTenantPackageExists(Long id) {
		TenantPackage tenantPackage = this.getById(id);
		if (Objects.isNull(tenantPackage)) {
			throw new FxzException("套餐信息不存在！更新失败！");
		}

		return tenantPackage;
	}

	private void validTenantPackageUsed(Long id) {
		Tenant tenant = tenantService
				.getOne(Wrappers.<Tenant>lambdaQuery().eq(Tenant::getPackageId, id).last("limit 1"));
		if (Objects.nonNull(tenant)) {
			throw new FxzException("套餐信息使用！");
		}
	}

	/**
	 * 分页查询租户套餐信息
	 */
	@Override
	public IPage<TenantPackage> pageTenantPackage(Page<TenantPackage> pageParam, TenantPackageParam param) {
		return tenantPackageMapper.selectPage(pageParam, param.lambdaQuery());
	}

	/**
	 * 获取单条
	 */
	@Override
	public TenantPackage findById(Long id) {
		return tenantPackageMapper.selectById(id);
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<TenantPackage> findAll() {
		return tenantPackageMapper.selectList(Wrappers.emptyWrapper());
	}

}