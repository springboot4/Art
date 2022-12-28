package com.art.common.websocket.core.support.impl.resolver;

import com.art.common.websocket.core.support.MethodArgumentResolver;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import org.springframework.core.MethodParameter;

public class HttpHeadersMethodArgumentResolver implements MethodArgumentResolver {

	@Override
	public boolean support(MethodParameter parameter) {
		return HttpHeaders.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
		return ((FullHttpRequest) object).headers();
	}

}
