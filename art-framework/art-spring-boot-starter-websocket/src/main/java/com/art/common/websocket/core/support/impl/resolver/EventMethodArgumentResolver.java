package com.art.common.websocket.core.support.impl.resolver;

import com.art.common.websocket.core.annotation.OnEvent;
import com.art.common.websocket.core.support.MethodArgumentResolver;
import io.netty.channel.Channel;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.core.MethodParameter;

import java.util.Objects;

public class EventMethodArgumentResolver implements MethodArgumentResolver {

	private final AbstractBeanFactory beanFactory;

	public EventMethodArgumentResolver(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	public boolean support(MethodParameter parameter) {
		return Objects.requireNonNull(parameter.getMethod()).isAnnotationPresent(OnEvent.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
		if (object == null) {
			return null;
		}

		TypeConverter typeConverter = beanFactory.getTypeConverter();
		return typeConverter.convertIfNecessary(object, parameter.getParameterType());
	}

}
