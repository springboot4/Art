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

package com.art.system.controller;

import com.art.common.log.core.annotation.OperLogAnn;
import com.art.common.log.core.enums.BusinessType;
import com.art.common.core.model.PageResult;
import com.art.common.core.model.Result;
import com.art.system.api.dict.dto.DictDTO;
import com.art.system.api.dict.dto.DictItemDTO;
import com.art.system.api.dict.dto.DictPageDTO;
import com.art.system.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典表
 *
 * @author fxz
 * @date 2022-04-04
 */
@Tag(name = "字典管理")
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class DictController {

	private final DictService dictService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@OperLogAnn(title = "字典管理", businessType = BusinessType.INSERT)
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody DictDTO dictDto) {
		return Result.success(dictService.addDict(dictDto));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@OperLogAnn(title = "字典管理", businessType = BusinessType.UPDATE)
	@PostMapping(value = "/update")
	public Result<Void> update(@RequestBody DictDTO dictDto) {
		return Result.judge(dictService.updateDict(dictDto));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@OperLogAnn(title = "字典管理", businessType = BusinessType.DELETE)
	@DeleteMapping(value = "/delete")
	public Result<Void> delete(Long id) {
		return Result.judge(dictService.deleteDict(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	public Result<DictDTO> findById(Long id) {
		return Result.success(dictService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@OperLogAnn(title = "字典管理", businessType = BusinessType.OTHER)
	@GetMapping(value = "/findAll")
	public Result<List<DictDTO>> findAll() {
		return Result.success(dictService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	public Result<PageResult<DictDTO>> pageDict(DictPageDTO dictPageDTO) {
		return Result.success(PageResult.success(dictService.pageDict(dictPageDTO)));
	}

	/**
	 * 根据字典类型获取字典下的所有字典项
	 * @param type 字典类型
	 * @return 字典项
	 */
	@Operation(summary = "根据字典类型获取字典下的所有字典项")
	@GetMapping("/getDictItemsByType")
	public Result<List<DictItemDTO>> getDictItemsByType(String type) {
		return Result.success(dictService.getDictItemsByType(type));
	}

}