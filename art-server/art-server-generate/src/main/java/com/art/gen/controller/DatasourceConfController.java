/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.gen.controller;

import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;

import com.art.gen.dao.dataobject.DatasourceConfDO;
import com.art.gen.service.impl.MyDatasourceConfServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
	public Result<Boolean> addDs(@RequestBody DatasourceConfDO datasourceConf) {
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
	public Result<Boolean> updateDsConf(@RequestBody DatasourceConfDO datasourceConf) {
		return Result.judge(datasourceConfService.updateDsConf(datasourceConf));
	}

	/**
	 * 根据id查询数据源信息
	 * @param id 数据源id
	 * @return 数据源信息
	 */
	@GetMapping("/findById")
	public Result<DatasourceConfDO> findById(@RequestParam("id") Long id) {
		return Result.success(datasourceConfService.findBtId(id));
	}

	/**
	 * 分页查询数据源管理信息
	 * @param page 分页参数
	 * @return 分页数据
	 */
	@GetMapping(value = "/page")
	public Result<PageResult<DatasourceConfDO>> page(Page<DatasourceConfDO> page) {
		Page<DatasourceConfDO> result = datasourceConfService.pageDataSourceConf(page, Wrappers.emptyWrapper());
		return Result.success(PageResult.success(result));
	}

	/**
	 * 查询所有数据源信息
	 */
	@GetMapping("/listDs")
	public Result<List<DatasourceConfDO>> listDs() {
		return Result.success(datasourceConfService.listDs());
	}

}