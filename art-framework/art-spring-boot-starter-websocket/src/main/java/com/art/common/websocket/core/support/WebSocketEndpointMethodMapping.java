package com.art.common.websocket.core.support;

import com.art.common.websocket.core.annotation.*;
import com.art.common.websocket.core.exception.WebSocketException;
import com.art.common.websocket.core.support.impl.resolver.*;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fxz websocket端点方法映射
 */
public class WebSocketEndpointMethodMapping {

	/**
	 * 参数名解析
	 */
	private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

	/**
	 * 握手前方法回调方法
	 */
	private final Method beforeHandshake;

	/**
	 * 连接完成回调方法
	 */
	private final Method onOpen;

	/**
	 * 连接关闭回调方法
	 */
	private final Method onClose;

	/**
	 * 连接错误回调方法
	 */
	private final Method onError;

	/**
	 * 接收到到Netty事件回调方法
	 */
	private final Method onEvent;

	/**
	 * 接收消息回调方法
	 */
	private final Method onMessage;

	/**
	 * 接收二进制消息回调方法
	 */
	private final Method onBinary;

	/**
	 * 握手前回调方法参数
	 */
	private final MethodParameter[] beforeHandshakeParameters;

	/**
	 * 连接建立回调方法参数
	 */
	private final MethodParameter[] onOpenParameters;

	/**
	 * 连接关闭方法参数
	 */
	private final MethodParameter[] onCloseParameters;

	/**
	 * 连接错误方法参数
	 */
	private final MethodParameter[] onErrorParameters;

	/**
	 * 接收字符串消息回调方法参数
	 */
	private final MethodParameter[] onMessageParameters;

	/**
	 * 接收二进制消息回调方法参数
	 */
	private final MethodParameter[] onBinaryParameters;

	/**
	 * 接收Netty事件回调方法参数
	 */
	private final MethodParameter[] onEventParameters;

	/**
	 * 握手前回调方法参数解析
	 */
	private final MethodArgumentResolver[] beforeHandshakeArgResolvers;

	/**
	 * 连接建立方法参数解析
	 */
	private final MethodArgumentResolver[] onOpenArgResolvers;

	/**
	 * 连接关闭方法参数解析
	 */
	private final MethodArgumentResolver[] onCloseArgResolvers;

	/**
	 * 连接错误方法参数解析
	 */
	private final MethodArgumentResolver[] onErrorArgResolvers;

	/**
	 * 接收到字符串消息参数方法参数解析
	 */
	private final MethodArgumentResolver[] onMessageArgResolvers;

	/**
	 * 接收到二进制消息方法参数解析
	 */
	private final MethodArgumentResolver[] onBinaryArgResolvers;

	/**
	 * 接收到Netty事件消息方法参数解析
	 */
	private final MethodArgumentResolver[] onEventArgResolvers;

	/**
	 * 标识了@WebSocketEndpoint注解的端点类
	 */
	private final Class<?> webSocketEndpointClazz;

	private final ApplicationContext applicationContext;

	private final AbstractBeanFactory beanFactory;

	public WebSocketEndpointMethodMapping(Class<?> webSocketEndpointClazz, ApplicationContext context,
			AbstractBeanFactory beanFactory) throws WebSocketException {
		this.applicationContext = context;
		this.webSocketEndpointClazz = webSocketEndpointClazz;
		this.beanFactory = beanFactory;

		Method handshake = null;
		Method open = null;
		Method close = null;
		Method error = null;
		Method message = null;
		Method binary = null;
		Method event = null;
		Method[] webSocketClazzMethods = webSocketEndpointClazz.getDeclaredMethods();
		Class<?> currentClazz = webSocketEndpointClazz;

		while (!currentClazz.equals(Object.class)) {
			// 当前类声明的方法
			Method[] currentClazzMethods = currentClazz.getDeclaredMethods();

			// 遍历方法获取添加了注解的方法
			for (Method method : currentClazzMethods) {
				if (method.getAnnotation(BeforeHandshake.class) != null) {
					check(method);

					if (handshake == null) {
						handshake = method;
					}
					else {
						// （1）说明了一个类中多个方法带有BeforeHandshake注解
						// （2）说明了不是父类BeforeHandshake方法的重写
						if (currentClazz == webSocketEndpointClazz || !isMethodOverride(handshake, method)) {
							throw new WebSocketException("重复添加注解: @BeforeHandshake");
						}
					}
				}
				else if (method.getAnnotation(OnOpen.class) != null) {
					check(method);

					if (open == null) {
						open = method;
					}
					else {
						// （1）说明了一个类中多个方法带有OnOpen注解 （2）说明了不是父类OnOpen方法的重写
						if (currentClazz == webSocketEndpointClazz || !isMethodOverride(open, method)) {
							throw new WebSocketException("重复添加注解: @OnOpen");
						}
					}
				}
				else if (method.getAnnotation(OnClose.class) != null) {
					check(method);

					if (close == null) {
						close = method;
					}
					else {
						// （1）说明了一个类中多个方法带有OnClose注解 （2）说明了不是父类OnClose方法的重写
						if (currentClazz == webSocketEndpointClazz || !isMethodOverride(close, method)) {
							throw new WebSocketException("重复添加注解: @OnClose");
						}
					}
				}
				else if (method.getAnnotation(OnError.class) != null) {
					check(method);

					if (error == null) {
						error = method;
					}
					else {
						// （1）说明了一个类中多个方法带有OnError注解 （2）说明了不是父类OnError方法的重写
						if (currentClazz == webSocketEndpointClazz || !isMethodOverride(error, method)) {
							throw new WebSocketException("重复添加注解: @OnError");
						}
					}
				}
				else if (method.getAnnotation(OnMessage.class) != null) {
					check(method);

					if (message == null) {
						message = method;
					}
					else {
						// （1）说明了一个类中多个方法带有OnMessage注解 （2）说明了不是父类OnMessage方法的重写
						if (currentClazz == webSocketEndpointClazz || !isMethodOverride(message, method)) {
							throw new WebSocketException("重复添加注解: @onMessage");
						}
					}
				}
				else if (method.getAnnotation(OnBinary.class) != null) {
					check(method);

					if (binary == null) {
						binary = method;
					}
					else {
						// （1）说明了一个类中多个方法带有OnBinary注解 （2）说明了不是父类OnBinary方法的重写
						if (currentClazz == webSocketEndpointClazz || !isMethodOverride(binary, method)) {
							throw new WebSocketException("重复添加注解: @OnBinary");
						}
					}
				}
				else if (method.getAnnotation(OnEvent.class) != null) {
					check(method);

					if (event == null) {
						event = method;
					}
					else {
						if (currentClazz == webSocketEndpointClazz || !isMethodOverride(event, method)) {
							throw new WebSocketException("重复添加注解: @OnEvent");
						}
					}
				}
			}

			// 继续获取父类信息
			currentClazz = currentClazz.getSuperclass();
		}

		// 父类标注了注解但是此websocket端点未标注此注解 取消标记
		if (handshake != null && handshake.getDeclaringClass() != webSocketEndpointClazz) {
			if (isOverrideWithoutAnnotation(webSocketClazzMethods, handshake, BeforeHandshake.class)) {
				handshake = null;
			}
		}
		if (open != null && open.getDeclaringClass() != webSocketEndpointClazz) {
			if (isOverrideWithoutAnnotation(webSocketClazzMethods, open, OnOpen.class)) {
				open = null;
			}
		}
		if (close != null && close.getDeclaringClass() != webSocketEndpointClazz) {
			if (isOverrideWithoutAnnotation(webSocketClazzMethods, close, OnClose.class)) {
				close = null;
			}
		}
		if (error != null && error.getDeclaringClass() != webSocketEndpointClazz) {
			if (isOverrideWithoutAnnotation(webSocketClazzMethods, error, OnError.class)) {
				error = null;
			}
		}
		if (message != null && message.getDeclaringClass() != webSocketEndpointClazz) {
			if (isOverrideWithoutAnnotation(webSocketClazzMethods, message, OnMessage.class)) {
				message = null;
			}
		}
		if (binary != null && binary.getDeclaringClass() != webSocketEndpointClazz) {
			if (isOverrideWithoutAnnotation(webSocketClazzMethods, binary, OnBinary.class)) {
				binary = null;
			}
		}
		if (event != null && event.getDeclaringClass() != webSocketEndpointClazz) {
			if (isOverrideWithoutAnnotation(webSocketClazzMethods, event, OnEvent.class)) {
				event = null;
			}
		}

		this.beforeHandshake = handshake;
		this.onOpen = open;
		this.onClose = close;
		this.onError = error;
		this.onMessage = message;
		this.onBinary = binary;
		this.onEvent = event;

		// 获取方法参数
		beforeHandshakeParameters = getParameters(beforeHandshake);
		onOpenParameters = getParameters(onOpen);
		onCloseParameters = getParameters(onClose);
		onMessageParameters = getParameters(onMessage);
		onErrorParameters = getParameters(onError);
		onBinaryParameters = getParameters(onBinary);
		onEventParameters = getParameters(onEvent);

		// 获取方法参数解析器
		beforeHandshakeArgResolvers = getResolvers(beforeHandshakeParameters);
		onOpenArgResolvers = getResolvers(onOpenParameters);
		onCloseArgResolvers = getResolvers(onCloseParameters);
		onMessageArgResolvers = getResolvers(onMessageParameters);
		onErrorArgResolvers = getResolvers(onErrorParameters);
		onBinaryArgResolvers = getResolvers(onBinaryParameters);
		onEventArgResolvers = getResolvers(onEventParameters);
	}

	/**
	 * 判断方法签名是否一致
	 * @param method1 方法1
	 * @param method2 方方2
	 * @return true or false
	 */
	private boolean isMethodOverride(Method method1, Method method2) {
		return (method1.getName().equals(method2.getName()) && method1.getReturnType().equals(method2.getReturnType())
				&& Arrays.equals(method1.getParameterTypes(), method2.getParameterTypes()));
	}

	/**
	 * 子类重写方法时是否更改注解
	 * @param methods 子类的所有方法
	 * @param superClazzMethod 父类方法
	 * @param annotation annotation
	 * @return true or false
	 */
	private boolean isOverrideWithoutAnnotation(Method[] methods, Method superClazzMethod,
			Class<? extends Annotation> annotation) {
		for (Method method : methods) {
			// 父类方法上添加了注解且子类重写方法上不具有此注解
			if (isMethodOverride(method, superClazzMethod) && (method.getAnnotation(annotation) == null)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查方法访问权限是否公开
	 * @param m Method
	 */
	private void check(Method m) throws WebSocketException {
		if (!Modifier.isPublic(m.getModifiers())) {
			throw new WebSocketException("methodNotPublic:" + m.getName());
		}
	}

	Object getEndpointInstance()
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Object instance = webSocketEndpointClazz.getDeclaredConstructor().newInstance();
		AutowiredAnnotationBeanPostProcessor postProcessor = applicationContext
			.getBean(AutowiredAnnotationBeanPostProcessor.class);
		postProcessor.postProcessProperties(null, instance, null);
		return instance;
	}

	Method getBeforeHandshake() {
		return beforeHandshake;
	}

	Object[] getBeforeHandshakeArgs(Channel channel, FullHttpRequest req) throws Exception {
		return getMethodArgumentValues(channel, req, beforeHandshakeParameters, beforeHandshakeArgResolvers);
	}

	Method getOnOpen() {
		return onOpen;
	}

	Object[] getOnOpenArgs(Channel channel, FullHttpRequest req) throws Exception {
		return getMethodArgumentValues(channel, req, onOpenParameters, onOpenArgResolvers);
	}

	MethodArgumentResolver[] getOnOpenArgResolvers() {
		return onOpenArgResolvers;
	}

	Method getOnClose() {
		return onClose;
	}

	Object[] getOnCloseArgs(Channel channel) throws Exception {
		return getMethodArgumentValues(channel, null, onCloseParameters, onCloseArgResolvers);
	}

	Method getOnError() {
		return onError;
	}

	Object[] getOnErrorArgs(Channel channel, Throwable throwable) throws Exception {
		return getMethodArgumentValues(channel, throwable, onErrorParameters, onErrorArgResolvers);
	}

	Method getOnMessage() {
		return onMessage;
	}

	Object[] getOnMessageArgs(Channel channel, TextWebSocketFrame textWebSocketFrame) throws Exception {
		return getMethodArgumentValues(channel, textWebSocketFrame, onMessageParameters, onMessageArgResolvers);
	}

	Method getOnBinary() {
		return onBinary;
	}

	Object[] getOnBinaryArgs(Channel channel, BinaryWebSocketFrame binaryWebSocketFrame) throws Exception {
		return getMethodArgumentValues(channel, binaryWebSocketFrame, onBinaryParameters, onBinaryArgResolvers);
	}

	Method getOnEvent() {
		return onEvent;
	}

	Object[] getOnEventArgs(Channel channel, Object evt) throws Exception {
		return getMethodArgumentValues(channel, evt, onEventParameters, onEventArgResolvers);
	}

	/**
	 * Get all params
	 * @param channel Channel
	 * @param object Object
	 * @param parameters MethodParameter[]
	 * @param resolvers MethodArgumentResolver
	 * @return Object[]
	 */
	private Object[] getMethodArgumentValues(Channel channel, Object object, MethodParameter[] parameters,
			MethodArgumentResolver[] resolvers) throws Exception {
		Object[] objects = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			MethodParameter parameter = parameters[i];
			MethodArgumentResolver resolver = resolvers[i];
			Object arg = resolver.resolveArgument(parameter, channel, object);
			objects[i] = arg;
		}
		return objects;
	}

	/**
	 * 获取方法参数解析器
	 * @param parameters MethodParameter[]
	 * @return MethodArgumentResolver[]
	 */
	private MethodArgumentResolver[] getResolvers(MethodParameter[] parameters) throws WebSocketException {
		MethodArgumentResolver[] methodArgumentResolvers = new MethodArgumentResolver[parameters.length];
		List<MethodArgumentResolver> resolvers = getDefaultResolvers();

		for (int i = 0; i < parameters.length; i++) {
			MethodParameter parameter = parameters[i];
			for (MethodArgumentResolver resolver : resolvers) {
				if (resolver.support(parameter)) {
					methodArgumentResolvers[i] = resolver;
					break;
				}
			}

			if (methodArgumentResolvers[i] == null) {
				throw new WebSocketException("参数无法解析 : " + parameter.getParameterName());
			}
		}

		return methodArgumentResolvers;
	}

	/**
	 * 默认方法参数解析器
	 * @return List<MethodArgumentResolver>
	 */
	private List<MethodArgumentResolver> getDefaultResolvers() {
		List<MethodArgumentResolver> resolvers = new ArrayList<>();
		resolvers.add(new SessionMethodArgumentResolver());
		resolvers.add(new HttpHeadersMethodArgumentResolver());
		resolvers.add(new TextMethodArgumentResolver());
		resolvers.add(new ThrowableMethodArgumentResolver());
		resolvers.add(new ByteMethodArgumentResolver());
		resolvers.add(new RequestParamMapMethodArgumentResolver());
		resolvers.add(new RequestParamMethodArgumentResolver(beanFactory));
		resolvers.add(new PathVariableMapMethodArgumentResolver());
		resolvers.add(new PathVariableMethodArgumentResolver(beanFactory));
		resolvers.add(new EventMethodArgumentResolver(beanFactory));
		return resolvers;
	}

	/**
	 * 获取方法参数
	 * @param method Method
	 * @return Method params
	 */
	private static MethodParameter[] getParameters(Method method) {
		if (method == null) {
			return new MethodParameter[0];
		}

		int paramsCount = method.getParameterCount();
		MethodParameter[] result = new MethodParameter[paramsCount];

		for (int i = 0; i < paramsCount; i++) {
			MethodParameter methodParameter = new MethodParameter(method, i);
			methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
			result[i] = methodParameter;
		}

		return result;
	}

}