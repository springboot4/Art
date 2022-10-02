package com.fxz.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.system.entity.Tenant;
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
	 * 分页
	 */
	IPage<Tenant> pageSysTenant(Page pageParam, Tenant tenant);

	/**
	 * 获取全部
	 */
	List<Tenant> findAll();

	/**
	 * 删除
	 */
	Boolean deleteSysTenant(Long id);

}