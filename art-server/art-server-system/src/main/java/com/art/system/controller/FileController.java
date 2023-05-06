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

import cn.hutool.core.lang.Dict;
import com.art.common.core.model.PageResult;
import com.art.common.core.model.Result;
import com.art.common.log.core.annotation.OperLogAnn;
import com.art.common.log.core.enums.BusinessType;
import com.art.common.security.core.annotation.Ojbk;
import com.art.system.api.file.dto.FileDTO;
import com.art.system.api.file.dto.FileDownloadDTO;
import com.art.system.api.file.dto.FilePageDTO;
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
	public Result<Dict> add(@RequestPart("file") MultipartFile file) {
		return Result.success(fileService.addFile(file));
	}

	/**
	 * 下载文件
	 * @param response 响应
	 */
	@Operation(summary = "下载文件")
	@PostMapping("/download")
	public void download(@RequestBody FileDownloadDTO fileDownloadDTO, HttpServletResponse response) {
		fileService.getFile(fileDownloadDTO.getBucket(), fileDownloadDTO.getFileName(), response);
	}

	/**
	 * 下载文件
	 * @param response 响应
	 */
	@Ojbk
	@Operation(summary = "下载文件")
	@GetMapping("/download/{bucket}/{fileName}")
	public void download(@PathVariable("bucket") String bucket, @PathVariable("fileName") String fileName,
			HttpServletResponse response) {
		fileService.getFile(bucket, fileName, response);
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody FileDTO fileDto) {
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
	public Result<FileDTO> findById(Long id) {
		return Result.success(fileService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	public Result<List<FileDTO>> findAll() {
		return Result.success(fileService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	public Result<PageResult<FileDTO>> pageFile(FilePageDTO filePageDTO) {
		return Result.success(PageResult.success(fileService.pageFile(filePageDTO)));
	}

	/**
	 * 获取文件临时外链
	 */
	@Operation(summary = "获取文件临时外链")
	@PostMapping(value = "/preSignUploadUrl")
	public Result<String> preSignUploadUrl(@RequestBody FileDownloadDTO dto) {
		return Result.success(fileService.preSignUploadUrl(dto));
	}

}