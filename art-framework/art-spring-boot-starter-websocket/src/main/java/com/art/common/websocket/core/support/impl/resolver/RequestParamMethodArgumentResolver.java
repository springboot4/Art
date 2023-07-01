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
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.core.MethodParameter;

import java.util.List;
import java.util.Map;

import static com.art.common.websocket.core.support.WebSocketEndpointEventServer.REQUEST_PARAM;

public class RequestParamMethodArgumentResolver implements MethodArgumentResolver {

	private AbstractBeanFactory beanFactory;

	public RequestParamMethodArgumentResolver(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	public boolean support(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
		RequestParam ann = parameter.getParameterAnnotation(RequestParam.class);
		assert ann != null;
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
		List<String> arg = (requestParams != null ? requestParams.get(name) : null);
		TypeConverter typeConverter = beanFactory.getTypeConverter();
		if (arg == null) {
			if ("\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n".equals(ann.defaultValue())) {
				return null;
			}
			else {
				return typeConverter.convertIfNecessary(ann.defaultValue(), parameter.getParameterType());
			}
		}
		if (List.class.isAssignableFrom(parameter.getParameterType())) {
			return typeConverter.convertIfNecessary(arg, parameter.getParameterType());
		}
		else {
			return typeConverter.convertIfNecessary(arg.get(0), parameter.getParameterType());
		}
	}

}
