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

package com.art.auth.api.token;

import com.art.common.feign.core.annotation.ArtBackoff;
import com.art.common.feign.core.annotation.ArtFeignRetry;
import com.art.core.common.constant.ArtServerConstants;
import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;
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
@FeignClient(contextId = "tokenServiceApi", value = ArtServerConstants.ART_AUTH)
public interface TokenServiceApi {

	/**
	 * 删除token
	 */
	@DeleteMapping("/token/{token}")
	Result<Void> removeToken(@PathVariable("token") String token);

	/**
	 * 分页查询token
	 */
	@ArtFeignRetry(maxAttempt = 5, backoff = @ArtBackoff(delay = 500L))
	@PostMapping("/token/page")
	Result<PageResult> tokenList(@RequestBody Map<String, Object> params);

}
