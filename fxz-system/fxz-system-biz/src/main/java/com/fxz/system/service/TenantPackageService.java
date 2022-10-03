package com.fxz.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.system.entity.TenantPackage;
import com.fxz.system.param.TenantPackageParam;

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