package com.fxz.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.common.mp.result.PageResult;
import com.fxz.common.mp.result.Result;
import com.fxz.common.security.annotation.Ojbk;
import com.fxz.system.entity.Tenant;
import com.fxz.system.param.TenantParam;
import com.fxz.system.service.TenantService;
import com.fxz.system.vo.TenantVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户表
 *
 * @author fxz
 * @date 2022-10-01
 */
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
public class TenantController {

	private final TenantService tenantService;

	/**
	 * 获取所有租户id集合
	 * @return 所有租户id集合
	 */
	@GetMapping(value = "/getTenantIds")
	public List<Long> getTenantIds() {
		return tenantService.getTenantIds();
	}

	/**
	 * 校验租户信息是否合法
	 * @param id 租户id
	 */
	@Ojbk
	@GetMapping(value = "/validTenant/{id}")
	public void validTenant(@PathVariable("id") Long id) {
		tenantService.validTenant(id);
	}

	/**
	 * 保存租户信息
	 * @param tenant 租户视图信息
	 */
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody TenantVO tenant) {
		return Result.success(tenantService.addSysTenant(tenant));
	}

	/**
	 * 修改租户信息
	 */
	@PostMapping(value = "/update")
	public Result update(@RequestBody Tenant tenant) {
		return Result.success(tenantService.updateSysTenant(tenant));
	}

	/**
	 * 删除租户信息
	 */
	@DeleteMapping(value = "/delete")
	public Result delete(Long id) {
		return Result.judge(tenantService.deleteSysTenant(id));
	}

	/**
	 * 根据id查询租户信息
	 */
	@GetMapping(value = "/findById")
	public Result<Tenant> findById(Long id) {
		return Result.success(tenantService.findById(id));
	}

	/**
	 * 根据name查询租户信息
	 */
	@Ojbk
	@GetMapping(value = "/findIdByName")
	public Result<Long> findTenantIdById(String name) {
		return Result.success(tenantService.findTenantIdById(name));
	}

	/**
	 * 获取全部租户信息
	 */
	@GetMapping(value = "/findAll")
	public Result<List<Tenant>> findAll() {
		return Result.success(tenantService.findAll());
	}

	/**
	 * 分页查询租户列表
	 */
	@GetMapping(value = "/page")
	public Result<PageResult<Tenant>> pageSysTenant(Page pageParam, TenantParam param) {
		return Result.success(PageResult.success(tenantService.pageSysTenant(pageParam, param)));
	}

}