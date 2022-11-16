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
import com.art.common.log.annotation.OperLogAnn;
import com.art.common.log.enums.BusinessType;
import com.art.common.mp.result.PageResult;
import com.art.common.mp.result.Result;
import com.art.common.security.annotation.Ojbk;
import com.art.system.dto.FileDto;
import com.art.system.entity.File;
import com.art.system.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件管理
 *
 * @author fxz
 * @date 2022-04-04
 */
@Tag(name = "文件管理")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	/**
	 * 上传文件
	 */
	@Operation(summary = "上传文件")
	@OperLogAnn(title = "文件管理", businessType = BusinessType.INSERT)
	@PostMapping(value = "/add")
	public Result<Object> add(@RequestPart("file") MultipartFile file) {
		return Result.success(fileService.addFile(file));
	}

	/**
	 * 下载文件
	 * @param bucket 桶名称
	 * @param fileName 文件名
	 * @param response 响应
	 */
	@Operation(summary = "下载文件")
	@Ojbk
	@GetMapping("/{bucket}/{fileName}")
	public void file(@PathVariable String bucket, @PathVariable String fileName, HttpServletResponse response) {
		fileService.getFile(bucket, fileName, response);
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody FileDto fileDto) {
		return Result.success(fileService.updateFile(fileDto));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(fileService.deleteFile(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	public Result<File> findById(Long id) {
		return Result.success(fileService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	public Result<List<File>> findAll() {
		return Result.success(fileService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	public Result<PageResult<File>> pageFile(Page<File> pageParam, File file) {
		return Result.success(PageResult.success(fileService.pageFile(pageParam, file)));
	}

}