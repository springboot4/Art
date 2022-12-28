package com.art.common.websocket.core.support.impl.resolver;

import com.art.common.websocket.core.annotation.OnMessage;
import com.art.common.websocket.core.support.MethodArgumentResolver;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.core.MethodParameter;

public class TextMethodArgumentResolver implements MethodArgumentResolver {

	@Override
	public boolean support(MethodParameter parameter) {
		return parameter.getMethod().isAnnotationPresent(OnMessage.class)
				&& String.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
		TextWebSocketFrame textFrame = (TextWebSocketFrame) object;
		return textFrame.text();
	}

}
