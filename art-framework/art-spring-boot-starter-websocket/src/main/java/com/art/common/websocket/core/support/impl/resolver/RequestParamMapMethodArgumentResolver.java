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

package com.art.common.websocket.core.support.impl.resolver;

import com.art.common.websocket.core.annotation.RequestParam;
import com.art.common.websocket.core.support.MethodArgumentResolver;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.springframework.core.MethodParameter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.art.common.websocket.core.support.WebSocketEndpointEventServer.REQUEST_PARAM;

public class RequestParamMapMethodArgumentResolver implements MethodArgumentResolver {

	@Override
	public boolean support(MethodParameter parameter) {
		RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
		return (requestParam != null && Map.class.isAssignableFrom(parameter.getParameterType())
				&& !StringUtils.hasText(requestParam.value()));
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
		RequestParam ann = parameter.getParameterAnnotation(RequestParam.class);
		String name = ann.value();
		if (name.isEmpty()) {
			name = parameter.getParameterName();
			if (name == null) {
				throw new IllegalArgumentException(
						"Name for argument type [" + parameter.getNestedParameterType().getName()
								+ "] not available, and parameter name information not found in class file either.");
			}
		}

		if (!channel.hasAttr(REQUEST_PARAM)) {
			QueryStringDecoder decoder = new QueryStringDecoder(((FullHttpRequest) object).uri());
			channel.attr(REQUEST_PARAM).set(decoder.parameters());
		}

		Map<String, List<String>> requestParams = channel.attr(REQUEST_PARAM).get();
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>(requestParams);
		if (MultiValueMap.class.isAssignableFrom(parameter.getParameterType())) {
			return multiValueMap;
		}
		else {
			return multiValueMap.toSingleValueMap();
		}
	}

}
