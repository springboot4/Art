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

package com.art.common.core.support.interceptor;

import com.art.common.core.constant.FxzConstant;
import com.art.common.core.model.Result;
import com.art.common.core.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截器只放行通过网关的请求
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-28 13:12
 */
public class ArtServerProtectInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
		// 从请求头中获取 gateway Token
		String token = request.getHeader(FxzConstant.GATEWAY_TOKEN_HEADER);
		String gatewayToken = new String(Base64Utils.encode(FxzConstant.GATEWAY_TOKEN_VALUE.getBytes()));
		// 校验 gateway Token的正确性
		if (StringUtils.equals(gatewayToken, token)) {
			return true;
		}

		WebUtil.makeFailureResponse(response, Result.failed("Please get resources through the gateway!"));
		return false;
	}

}
