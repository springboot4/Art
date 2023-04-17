package com.art.auth.controller;

import cn.hutool.core.lang.Dict;
import com.art.common.core.model.Result;
import com.art.common.security.client.properties.GiteeProperties;
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
