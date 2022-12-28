package com.art.common.websocket.core.support;

import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

public interface MethodArgumentResolver {

	/**
	 * 判断此解析器是否支持给定的方法参数
	 */
	boolean support(MethodParameter parameter);

	@Nullable
	Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception;

}
