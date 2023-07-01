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

package com.art.auth.controller;

import cn.hutool.core.lang.Dict;
import com.art.common.security.client.properties.GiteeProperties;
import com.art.core.common.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/17 08:59
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration")
public class ConfigurationEndpoint {

	private final ObjectProvider<GiteeProperties> giteePropertiesObjectProvider;

	@GetMapping
	public Result<?> configuration() {
		Dict dict = Dict.create();
		giteePropertiesObjectProvider.ifAvailable(g -> dict.put("giteeAppid", g.getDefaultAppid()));
		return Result.success(dict);
	}

}
