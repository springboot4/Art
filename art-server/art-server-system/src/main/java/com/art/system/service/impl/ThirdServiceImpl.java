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

package com.art.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.art.common.security.core.model.ArtAuthUser;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.system.service.ThirdOperationService;
import com.art.system.service.ThirdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/17 11:14
 */
@RequiredArgsConstructor
@Service
public class ThirdServiceImpl implements ThirdService {

	private final List<ThirdOperationService> thirdOperationServices;

	/**
	 * 获取第三方平台的绑定信息
	 * @return 第三方平台的绑定信息
	 */
	@Override
	public Dict bindInfo() {
		ArtAuthUser user = SecurityUtil.getUser();
		Long userId = user.getUserId();
		Dict dict = Dict.create();

		thirdOperationServices.forEach(t -> dict.put(t.support(), t.getBindInfo(userId)));

		return dict;
	}

	/**
	 * 解除绑定
	 * @param type 三方平台类型
	 */
	@Override
	public void unBind(String type) {
		Optional<ThirdOperationService> optional = thirdOperationServices.stream()
			.filter(t -> t.support().equals(type))
			.findFirst();
		optional.ifPresent(ThirdOperationService::unBind);
	}

}
