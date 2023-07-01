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

package com.art.system.service.impl;

import com.art.system.api.log.dto.OperLogDTO;
import com.art.system.api.log.dto.OperLogPageDTO;
import com.art.system.core.convert.OperLogConvert;
import com.art.system.manager.OperLogManager;
import com.art.system.service.OperLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author fxz
 * @date 2022-04-07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperLogServiceImpl implements OperLogService {

	private final OperLogManager operLogManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addOperLog(OperLogDTO operLogDto) {
		return operLogManager.addOperLog(operLogDto) > 0;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateOperLog(OperLogDTO operLogDto) {
		return operLogManager.updateOperLogById(operLogDto) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<OperLogDTO> pageOperLog(OperLogPageDTO operLogPageDTO) {
		return OperLogConvert.INSTANCE.convert(operLogManager.pageOperLog(operLogPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public OperLogDTO findById(Long id) {
		return OperLogConvert.INSTANCE.convert(operLogManager.selectOperLogById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<OperLogDTO> findAll() {
		return OperLogConvert.INSTANCE.convert(operLogManager.listOperLog());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteOperLog(Long id) {
		return operLogManager.deleteOperLogById(id) > 0;
	}

}