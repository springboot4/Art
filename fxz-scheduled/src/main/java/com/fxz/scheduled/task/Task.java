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

package com.fxz.scheduled.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author fxz
 */
@Slf4j
@Component("fxzTask")
public class Task {

	public void fxzMultipleParamsTask(String s, Boolean b, Long l, Double d, Integer i) {
		log.info("执行多参方法:{},{},{},{},{}", s, b, l, d, i);
	}

	public void fxzParamsTask(String params) {
		log.info("执行有参方法：{}", params);
	}

	public void fxzNoParamsTask() {
		log.info("执行无参方法");
	}

}
