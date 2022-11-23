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

package com.art.system.api.token;

import com.art.common.core.constant.FxzServerConstant;
import com.art.common.mp.result.PageResult;
import com.art.common.mp.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-04-14 21:30
 */
@FeignClient(contextId = "tokenServiceApi", value = FxzServerConstant.FXZ_AUTH)
public interface TokenServiceApi {

	/**
	 * 删除token
	 */
	@DeleteMapping("/token/{token}")
	Result<Void> removeToken(@PathVariable("token") String token);

	/**
	 * 分页查询token
	 */
	@PostMapping("/token/page")
	Result<PageResult> tokenList(@RequestBody Map<String, Object> params);

}
