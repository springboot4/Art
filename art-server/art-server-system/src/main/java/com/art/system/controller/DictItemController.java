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

package com.art.system.controller;

import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;

import com.art.system.api.dict.dto.DictItemDTO;
import com.art.system.api.dict.dto.DictItemExistsDTO;
import com.art.system.api.dict.dto.DictItemPageDTO;
import com.art.system.service.DictItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典项
 *
 * @author fxz
 * @date 2022-04-04
 */
@Tag(name = "字典项管理")
@RestController
@RequestMapping("/dictItem")
@RequiredArgsConstructor
public class DictItemController {

	private final DictItemService dictItemService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	public Result<Void> add(@RequestBody DictItemDTO dictItemDto) {
		return Result.judge(dictItemService.addDictItem(dictItemDto));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	public Result<Void> update(@RequestBody DictItemDTO dictItemDto) {
		return Result.judge(dictItemService.updateDictItem(dictItemDto));
	}

	/**
	 * 删除
	 */
	@DeleteMapping(value = "/delete")
	public Result<Void> delete(Long id) {
		return Result.judge(dictItemService.deleteDictItem(id));
	}

	/**
	 * 获取单条
	 */
	@GetMapping(value = "/findById")
	public Result<DictItemDTO> findById(Long id) {
		return Result.success(dictItemService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@GetMapping(value = "/findAll")
	public Result<List<DictItemDTO>> findAll() {
		return Result.success(dictItemService.findAll());
	}

	/**
	 * 分页
	 */
	@GetMapping(value = "/page")
	public Result<PageResult<DictItemDTO>> pageDictItem(DictItemPageDTO dictItemPageDTO) {
		return Result.success(PageResult.success(dictItemService.pageDictItem(dictItemPageDTO)));
	}

	/**
	 * 校验字典项编码是否已经被使用
	 * @return true or false
	 */
	@GetMapping("/itemExistsByCode")
	public Result<Boolean> itemExistsByCode(DictItemExistsDTO dictItemExistsDTO) {
		return Result.success(dictItemService.itemExistsByCode(dictItemExistsDTO));
	}

}