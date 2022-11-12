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

package com.fxz.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.common.mp.result.Result;
import com.fxz.system.dto.FileDto;
import com.fxz.system.entity.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件管理表
 *
 * @author fxz
 * @date 2022-04-04
 */
public interface FileService extends IService<File> {

	/**
	 * 上传文件
	 */
	Result<Object> addFile(MultipartFile file);

	/**
	 * 修改
	 */
	Boolean updateFile(FileDto fileDto);

	/**
	 * 分页
	 */
	IPage<File> pageFile(Page<File> pageParam, File file);

	/**
	 * 获取单条
	 */
	File findById(Long id);

	/**
	 * 获取全部
	 */
	List<File> findAll();

	/**
	 * 删除
	 */
	Boolean deleteFile(Long id);

	/**
	 * 下载文件
	 * @param bucket 桶名称
	 * @param fileName 文件名
	 * @param response 响应
	 */
	void getFile(String bucket, String fileName, HttpServletResponse response);

}