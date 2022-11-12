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
import com.fxz.system.dto.OperLogDto;
import com.fxz.system.entity.OperLog;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author fxz
 * @date 2022-04-07
 */
public interface OperLogService extends IService<OperLog> {

	/**
	 * 添加
	 */
	Boolean addOperLog(OperLogDto operLogDto);

	/**
	 * 修改
	 */
	Boolean updateOperLog(OperLogDto operLogDto);

	/**
	 * 分页
	 */
	IPage<OperLog> pageOperLog(Page<OperLog> pageParam, OperLog operLog);

	/**
	 * 获取单条
	 */
	OperLog findById(Long id);

	/**
	 * 获取全部
	 */
	List<OperLog> findAll();

	/**
	 * 删除
	 */
	Boolean deleteOperLog(Long id);

}