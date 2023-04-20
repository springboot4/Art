/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

import com.art.common.core.model.PageResult;
import com.art.common.core.model.Result;
import com.art.common.security.core.annotation.Ojbk;
import com.art.system.api.client.dto.ClientDetailsDTO;
import com.art.system.api.client.dto.ClientDetailsPageDTO;
import com.art.system.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 终端信息表
 *
 * @author fxz
 * @date 2023-04-13
 */
@Tag(name = "终端信息表")
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

	private final ClientService clientService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	@PreAuthorize("@ps.hasPermission('system:ClientDetails:add')")
	public Result<Boolean> add(@RequestBody ClientDetailsDTO dto) {
		return Result.success(clientService.addClientDetails(dto));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	@PreAuthorize("@ps.hasPermission('system:ClientDetails:update')")
	public Result<Boolean> update(@RequestBody ClientDetailsDTO dto) {
		return Result.success(clientService.updateClientDetails(dto));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	@PreAuthorize("@ps.hasPermission('system:ClientDetails:delete')")
	public Result<Boolean> delete(String id) {
		return Result.judge(clientService.deleteClientDetails(id));
	}

	/**
	 * 获取单条
	 */
	@Ojbk
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	public Result<ClientDetailsDTO> findById(String id) {
		return Result.success(clientService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	@PreAuthorize("@ps.hasPermission('system:ClientDetails:view')")
	public Result<List<ClientDetailsDTO>> findAll() {
		return Result.success(clientService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	@PreAuthorize("@ps.hasPermission('system:ClientDetails:view')")
	public Result<PageResult<ClientDetailsDTO>> pageClientDetails(ClientDetailsPageDTO dto) {
		return Result.success(PageResult.success(clientService.pageClientDetails(dto)));
	}

}