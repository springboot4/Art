package com.fxz.gen.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.common.mp.result.PageResult;
import com.fxz.common.mp.result.Result;
import com.fxz.gen.entity.DatasourceConf;
import com.fxz.gen.service.impl.MyDatasourceConfServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源表
 *
 * @author fxz
 * @date 2022-03-31
 */
@RestController
@RequestMapping("/datasourceConf")
@RequiredArgsConstructor
public class DatasourceConfController {

	private final MyDatasourceConfServiceImpl datasourceConfService;

	/**
	 * 动态添加数据源
	 * @param datasourceConf 数据源信息
	 */
	@PostMapping("/addDs")
	public Result<Boolean> addDs(@RequestBody DatasourceConf datasourceConf) {
		return Result.judge(datasourceConfService.addDs(datasourceConf));
	}

	/**
	 * 删除数据源信息
	 * @param id 数据源主键
	 */
	@DeleteMapping("/delete")
	public Result<Boolean> delete(@RequestParam("id") Long id) {
		return Result.judge(datasourceConfService.delete(id));
	}

	/**
	 * 修改数据源信息
	 * @param datasourceConf 数据源信息
	 * @return ture Or false
	 */
	@PostMapping("/update")
	public Result<Boolean> updateDsConf(@RequestBody DatasourceConf datasourceConf) {
		return Result.judge(datasourceConfService.updateDsConf(datasourceConf));
	}

	/**
	 * 根据id查询数据源信息
	 * @param id 数据源id
	 * @return 数据源信息
	 */
	@GetMapping("/findById")
	public Result<DatasourceConf> findById(@RequestParam("id") Long id) {
		return Result.success(datasourceConfService.findBtId(id));
	}

	/**
	 * 分页查询数据源管理信息
	 * @param page 分页参数
	 * @return 分页数据
	 */
	@GetMapping(value = "/page")
	public Result<PageResult<DatasourceConf>> page(Page<DatasourceConf> page) {
		Page<DatasourceConf> result = datasourceConfService.pageDataSourceConf(page, Wrappers.emptyWrapper());
		return Result.success(PageResult.success(result));
	}

	/**
	 * 查询所有数据源信息
	 */
	@GetMapping("/listDs")
	public Result<List<DatasourceConf>> listDs() {
		return Result.success(datasourceConfService.listDs());
	}

}