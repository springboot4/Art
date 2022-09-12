package com.fxz.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.common.mp.result.PageResult;
import com.fxz.common.mp.result.Result;
import com.fxz.system.entity.App;
import com.fxz.system.param.AppParam;
import com.fxz.system.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统应用表
 *
 * @author fxz
 * @date 2022-09-12
 */
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppController {

	private final AppService appService;

	/**
	 * 添加
	 */
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody App app) {
		return Result.success(appService.addSysApp(app));
	}

	/**
	 * 修改
	 */
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody App app) {
		return Result.success(appService.updateSysApp(app));
	}

	/**
	 * 删除
	 */
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(appService.deleteSysApp(id));
	}

	/**
	 * 获取单条
	 */
	@GetMapping(value = "/findById")
	public Result<App> findById(Long id) {
		return Result.success(appService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@GetMapping(value = "/findAll")
	public Result<List<App>> findAll() {
		return Result.success(appService.findAll());
	}

	/**
	 * 分页
	 */
	@GetMapping(value = "/page")
	public Result<PageResult<App>> pageSysApp(Page<App> pageParam, AppParam appParam) {
		return Result.success(PageResult.success(appService.pageSysApp(pageParam, appParam)));
	}

}