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

package com.art.common.quartz.core.service;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/7 20:29
 */
public interface ArtJobLogService {

	/**
	 * 保存job执行日志
	 * @param jobBeanName jobBeanName
	 * @param jobMessage job日志信息
	 * @param ex 异常信息
	 */
	void addJobLog(String jobBeanName, String jobMessage, String ex);

}
