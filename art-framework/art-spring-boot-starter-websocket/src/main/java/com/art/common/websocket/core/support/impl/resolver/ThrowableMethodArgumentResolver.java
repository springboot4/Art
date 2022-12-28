package com.art.common.websocket.core.support.impl.resolver;

import com.art.common.websocket.core.annotation.OnError;
import com.art.common.websocket.core.support.MethodArgumentResolver;
import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;

public class ThrowableMethodArgumentResolver implements MethodArgumentResolver {

	@Override
	public boolean support(MethodParameter parameter) {
		return parameter.getMethod().isAnnotationPresent(OnError.class)
				&& Throwable.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
		if (object instanceof Throwable) {
			return object;
		}
		return null;
	}

}
