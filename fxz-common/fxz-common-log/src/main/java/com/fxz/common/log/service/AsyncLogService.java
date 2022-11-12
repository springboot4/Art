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

package com.fxz.common.log.service;

import cn.hutool.core.bean.BeanUtil;
import com.fxz.system.dto.OperLogDto;
import com.fxz.system.entity.OperLog;
import com.fxz.system.feign.RemoteLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

/**
 * 异步操作日志服务
 *
 * @author fxz
 */
@Slf4j
@RequiredArgsConstructor
public class AsyncLogService {

	private final RemoteLogService remoteLogService;

	/**
	 * 保存系统日志记录
	 */
	@Async
	public void saveSysLog(OperLog operLog) {
		log.info("调用异步方法:{}", Thread.currentThread().getId());
		OperLogDto operLogDto = new OperLogDto();
		BeanUtil.copyProperties(operLog, operLogDto);
		remoteLogService.add(operLogDto);
	}

}
