package com.fxz.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.common.mp.result.PageResult;
import com.fxz.common.mp.result.Result;
import com.fxz.system.entity.TenantPackage;
import com.fxz.system.param.TenantPackageParam;
import com.fxz.system.service.TenantPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户套餐表
 *
 * @author fxz
 * @date 2022-10-01
 */
@Tag(name = "租户套餐管理")
@RestController
@RequestMapping("/tenant/package")
@RequiredArgsConstructor
public class TenantPackageController {

	private final TenantPackageService tenantPackageService;

	/**
	 * 保存租户套餐信息
	 */
	@Operation(summary = "保存租户套餐信息")
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody TenantPackage tenantPackage) {
		return Result.success(tenantPackageService.addTenantPackage(tenantPackage));
	}

	/**
	 * 更新租户套餐信息
	 */
	@Operation(summary = "更新租户套餐信息")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody TenantPackage tenantPackage) {
		return Result.success(tenantPackageService.updateTenantPackage(tenantPackage));
	}

	/**
	 * 删除租户套餐信息
	 */
	@Operation(summary = "删除租户套餐信息")
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(tenantPackageService.deleteTenantPackage(id));
	}

	/**
	 * 获取单条租户套餐信息
	 */
	@Operation(summary = "获取单条租户套餐信息")
	@GetMapping(value = "/findById")
	public Result<TenantPackage> findById(Long id) {
		return Result.success(tenantPackageService.findById(id));
	}

	/**
	 * 获取全部租户套餐信息
	 */
	@Operation(summary = "获取全部租户套餐信息")
	@GetMapping(value = "/findAll")
	public Result<List<TenantPackage>> findAll() {
		return Result.success(tenantPackageService.findAll());
	}

	/**
	 * 分页查询租户套餐信息
	 */
	@Operation(summary = "分页查询租户套餐信息")
	@GetMapping(value = "/page")
	public Result<PageResult<TenantPackage>> pageTenantPackage(Page<TenantPackage> pageParam,
			TenantPackageParam param) {
		return Result.success(PageResult.success(tenantPackageService.pageTenantPackage(pageParam, param)));
	}

}