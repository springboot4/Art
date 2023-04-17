/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

import com.art.common.core.model.Result;
import com.art.system.service.ThirdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方平台相关信息
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/17 11:11
 */
@RequiredArgsConstructor
@RequestMapping("/third")
@RestController
public class ThirdController {

	private final ThirdService thirdService;

	/**
	 * 获取第三方平台的绑定信息
	 * @return 第三方平台的绑定信息
	 */
	@GetMapping("/bindInfo")
	public Result<?> bindInfo() {
		return Result.success(thirdService.bindInfo());
	}

	/**
	 * 解除绑定
	 * @param type 平台类型
	 */
	@PutMapping("/unBind")
	public Result<Void> unBind(String type) {
		thirdService.unBind(type);
		return Result.success();
	}

}
