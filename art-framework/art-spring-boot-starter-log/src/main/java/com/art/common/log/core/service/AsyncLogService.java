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

package com.art.common.log.core.service;

import com.art.system.api.log.LogServiceApi;
import com.art.system.api.log.dto.OperLogDTO;
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

	private final LogServiceApi logServiceApi;

	/**
	 * 保存系统日志记录
	 */
	@Async
	public void saveSysLog(OperLogDTO operLog) {
		log.info("调用异步方法:{}", Thread.currentThread().getId());
		logServiceApi.add(operLog);
	}

}
