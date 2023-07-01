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

package com.art.system.service;

import com.art.system.api.log.dto.OperLogDTO;
import com.art.system.api.log.dto.OperLogPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author fxz
 * @date 2022-04-07
 */
public interface OperLogService {

	/**
	 * 添加
	 */
	Boolean addOperLog(OperLogDTO operLogDto);

	/**
	 * 修改
	 */
	Boolean updateOperLog(OperLogDTO operLogDto);

	/**
	 * 分页
	 */
	IPage<OperLogDTO> pageOperLog(OperLogPageDTO operLogPageDTO);

	/**
	 * 获取单条
	 */
	OperLogDTO findById(Long id);

	/**
	 * 获取全部
	 */
	List<OperLogDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteOperLog(Long id);

}