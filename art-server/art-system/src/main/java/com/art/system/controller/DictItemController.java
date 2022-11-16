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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.art.common.mp.result.PageResult;
import com.art.common.mp.result.Result;
import com.art.system.dto.DictItemDto;
import com.art.system.entity.DictItem;
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
	public Result<Boolean> add(@RequestBody DictItemDto dictItemDto) {
		return Result.success(dictItemService.addDictItem(dictItemDto));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	public Result<Void> update(@RequestBody DictItemDto dictItemDto) {
		return dictItemService.updateDictItem(dictItemDto);
	}

	/**
	 * 删除
	 */
	@DeleteMapping(value = "/delete")
	public Result<Void> delete(Long id) {
		return dictItemService.deleteDictItem(id);
	}

	/**
	 * 获取单条
	 */
	@GetMapping(value = "/findById")
	public Result<DictItem> findById(Long id) {
		return Result.success(dictItemService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@GetMapping(value = "/findAll")
	public Result<List<DictItem>> findAll() {
		return Result.success(dictItemService.findAll());
	}

	/**
	 * 分页
	 */
	@GetMapping(value = "/page")
	public Result<PageResult<DictItem>> pageDictItem(Page<DictItem> pageParam, DictItem dictItem) {
		return Result.success(PageResult.success(dictItemService.pageDictItem(pageParam, dictItem)));
	}

	/**
	 * 校验字典项编码是否已经被使用
	 * @param id 字典项id
	 * @param dictId 字典id
	 * @param value 字典项编码
	 * @return true or false
	 */
	@GetMapping("/itemExistsByCode")
	public Result<Boolean> itemExistsByCode(Long id, Long dictId, String value) {
		return Result.success(dictItemService.itemExistsByCode(id, dictId, value));
	}

}