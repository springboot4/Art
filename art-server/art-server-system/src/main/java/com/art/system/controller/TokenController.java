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

import com.art.auth.api.token.TokenServiceApi;
import com.art.common.core.model.PageResult;
import com.art.common.core.model.Result;
import com.art.common.log.core.annotation.OperLogAnn;
import com.art.common.log.core.enums.BusinessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 令牌管理
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022-04-14 21:32
 */
@Tag(name = "令牌管理")
@RequiredArgsConstructor
@RestController
public class TokenController {

	private final TokenServiceApi tokenServiceApi;

	/**
	 * 删除token
	 */
	@Operation(summary = "删除token")
	@OperLogAnn(title = "强退", businessType = BusinessType.FORCE)
	@SneakyThrows
	@DeleteMapping("/token/{token}")
	public Result<Void> removeToken(@PathVariable("token") String token) {
		return tokenServiceApi.removeToken(token);
	}

	/**
	 * 分页查询token
	 * @param params 分页参数
	 * @return
	 */
	@Operation(summary = "分页查询token")
	@GetMapping("/token/page")
	public Result<PageResult> tokenList(@RequestParam Map<String, Object> params) {
		return tokenServiceApi.tokenList(params);
	}

}
