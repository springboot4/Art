package com.art.common.websocket.core.support.impl.resolver;

import com.art.common.websocket.core.support.MethodArgumentResolver;
import com.art.common.websocket.core.support.NettySession;
import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;

import static com.art.common.websocket.core.support.WebSocketEndpointEventServer.SESSION_KEY;

public class SessionMethodArgumentResolver implements MethodArgumentResolver {

	@Override
	public boolean support(MethodParameter parameter) {
		return NettySession.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
		return channel.attr(SESSION_KEY).get();
	}

}
