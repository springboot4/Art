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

package com.art.common.websocket.core.support;

import com.art.common.websocket.core.support.impl.path.AntPathMatcherWrapper;
import com.art.common.websocket.core.support.impl.path.DefaultPathMatcher;
import com.art.common.websocket.core.support.impl.resolver.PathVariableMapMethodArgumentResolver;
import com.art.common.websocket.core.support.impl.resolver.PathVariableMethodArgumentResolver;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.TypeMismatchException;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 端点事件处理
 *
 * @author fxz
 */
public class WebSocketEndpointEventServer {

	/**
	 * 端点实例key
	 */
	private static final AttributeKey<Object> WEB_SOCKET_KEY = AttributeKey.valueOf("WEBSOCKET_IMPLEMENT");

	/**
	 * session key
	 */
	public static final AttributeKey<NettySession> SESSION_KEY = AttributeKey.valueOf("WEBSOCKET_SESSION");

	/**
	 * 端点路径key
	 */
	private static final AttributeKey<String> PATH_KEY = AttributeKey.valueOf("WEBSOCKET_PATH");

	/**
	 * websocket_uri_template
	 */
	public static final AttributeKey<Map<String, String>> URI_TEMPLATE = AttributeKey.valueOf("WEBSOCKET_URI_TEMPLATE");

	/**
	 * websocket_request_param
	 */
	public static final AttributeKey<Map<String, List<String>>> REQUEST_PARAM = AttributeKey
		.valueOf("WEBSOCKET_REQUEST_PARAM");

	/**
	 * 路径和端点方法映射
	 */
	private final Map<String, WebSocketEndpointMethodMapping> pathMethodMappingMap = new HashMap<>();

	/**
	 * WebSocket配置
	 */
	private final WebSocketEndpointConfig config;

	/**
	 * WebSocket path matcher
	 */
	private final Set<WsPathMatcher> pathMatchers = new HashSet<>();

	private static final InternalLogger INTERNAL_LOGGER = InternalLoggerFactory
		.getInstance(WebSocketEndpointEventServer.class);

	public WebSocketEndpointEventServer(WebSocketEndpointMethodMapping methodMapping, WebSocketEndpointConfig config,
			String path) {
		// 保存方法和路径的映射
		addPathMethodMapping(path, methodMapping);
		this.config = config;
	}

	/**
	 * 握手前是否执行回调方法
	 */
	public boolean hasBeforeHandshake(Channel channel, String path) {
		WebSocketEndpointMethodMapping methodMapping = getWebSocketMethodMapping(path, channel);
		return !Objects.isNull(methodMapping.getBeforeHandshake());
	}

	/**
	 * 执行握手前回调方法
	 */
	public void doBeforeHandshake(Channel channel, FullHttpRequest req, String path) {
		WebSocketEndpointMethodMapping methodMapping = getWebSocketMethodMapping(path, channel);

		Object implement;
		try {
			// 反射获取端点实例
			implement = methodMapping.getEndpointInstance();
		}
		catch (Exception e) {
			INTERNAL_LOGGER.error(e);
			return;
		}

		// 维护session和端点实例信息
		channel.attr(WEB_SOCKET_KEY).set(implement);
		NettySession nettySession = new NettySession(channel);
		channel.attr(SESSION_KEY).set(nettySession);

		Method beforeHandshake = methodMapping.getBeforeHandshake();
		if (Objects.isNull(beforeHandshake)) {
			return;
		}

		try {
			// 执行握手前回调方法
			beforeHandshake.invoke(implement, methodMapping.getBeforeHandshakeArgs(channel, req));
		}
		catch (TypeMismatchException e) {
			throw e;
		}
		catch (Throwable t) {
			INTERNAL_LOGGER.error(t);
		}
	}

	/**
	 * 连接建立成功回调方法
	 */
	public void doOnOpen(Channel channel, FullHttpRequest req, String path) {
		WebSocketEndpointMethodMapping methodMapping = getWebSocketMethodMapping(path, channel);

		Object implement = channel.attr(WEB_SOCKET_KEY).get();
		if (Objects.isNull(implement)) {
			try {
				// 反射获取端点实例
				implement = methodMapping.getEndpointInstance();
				channel.attr(WEB_SOCKET_KEY).set(implement);
			}
			catch (Exception e) {
				INTERNAL_LOGGER.error(e);
				return;
			}
			NettySession nettySession = new NettySession(channel);
			channel.attr(SESSION_KEY).set(nettySession);
		}

		Method onOpenMethod = methodMapping.getOnOpen();
		if (Objects.isNull(onOpenMethod)) {
			return;
		}

		try {
			// 执行连接前回调
			onOpenMethod.invoke(implement, methodMapping.getOnOpenArgs(channel, req));
		}
		catch (TypeMismatchException e) {
			throw e;
		}
		catch (Throwable t) {
			INTERNAL_LOGGER.error(t);
		}
	}

	/**
	 * 连接关闭方法回调
	 */
	public void doOnClose(Channel channel) {
		Attribute<String> attrPath = channel.attr(PATH_KEY);
		WebSocketEndpointMethodMapping methodMapping;

		if (pathMethodMappingMap.size() == 1) {
			methodMapping = pathMethodMappingMap.values().iterator().next();
		}
		else {
			String path = attrPath.get();
			methodMapping = pathMethodMappingMap.get(path);
			if (methodMapping == null) {
				return;
			}
		}

		if (Objects.isNull(methodMapping.getOnClose())) {
			return;
		}

		if (!channel.hasAttr(SESSION_KEY)) {
			return;
		}

		Object implement = channel.attr(WEB_SOCKET_KEY).get();
		try {
			// 执行关闭前回调
			methodMapping.getOnClose().invoke(implement, methodMapping.getOnCloseArgs(channel));
		}
		catch (Throwable t) {
			INTERNAL_LOGGER.error(t);
		}
	}

	/**
	 * 连接错误方法回调
	 */
	public void doOnError(Channel channel, Throwable throwable) {
		WebSocketEndpointMethodMapping methodMapping = getWebSocketMethodMapping(channel);
		if (Objects.isNull(methodMapping.getOnError())) {
			return;
		}

		if (!channel.hasAttr(SESSION_KEY)) {
			return;
		}

		Object implement = channel.attr(WEB_SOCKET_KEY).get();
		try {
			methodMapping.getOnError().invoke(implement, methodMapping.getOnErrorArgs(channel, throwable));
		}
		catch (Throwable t) {
			INTERNAL_LOGGER.error(t);
		}
	}

	/**
	 * 接收到字符串类型消息回调
	 */
	public void doOnMessage(Channel channel, WebSocketFrame frame) {
		WebSocketEndpointMethodMapping methodMapping = getWebSocketMethodMapping(channel);
		if (Objects.isNull(methodMapping.getOnMessage())) {
			return;
		}

		TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
		Object implement = channel.attr(WEB_SOCKET_KEY).get();
		try {
			methodMapping.getOnMessage().invoke(implement, methodMapping.getOnMessageArgs(channel, textFrame));
		}
		catch (Throwable t) {
			INTERNAL_LOGGER.error(t);
		}
	}

	/**
	 * 接收二进制消息回调方法
	 */
	public void doOnBinary(Channel channel, WebSocketFrame frame) {
		WebSocketEndpointMethodMapping methodMapping = getWebSocketMethodMapping(channel);
		if (Objects.isNull(methodMapping.getOnBinary())) {
			return;
		}

		BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
		Object implement = channel.attr(WEB_SOCKET_KEY).get();
		try {
			methodMapping.getOnBinary().invoke(implement, methodMapping.getOnBinaryArgs(channel, binaryWebSocketFrame));
		}
		catch (Throwable t) {
			INTERNAL_LOGGER.error(t);
		}
	}

	/**
	 * netty事件方法回调
	 */
	public void doOnEvent(Channel channel, Object evt) {
		WebSocketEndpointMethodMapping methodMapping = getWebSocketMethodMapping(channel);
		if (Objects.isNull(methodMapping.getOnEvent())) {
			return;
		}

		if (!channel.hasAttr(SESSION_KEY)) {
			return;
		}
		Object implement = channel.attr(WEB_SOCKET_KEY).get();
		try {
			methodMapping.getOnEvent().invoke(implement, methodMapping.getOnEventArgs(channel, evt));
		}
		catch (Throwable t) {
			INTERNAL_LOGGER.error(t);
		}
	}

	/**
	 * 保存路径与端点方法映射
	 */
	public void addPathMethodMapping(String path, WebSocketEndpointMethodMapping webSocketEndpointMethodMapping) {
		// 保存路径与端点方法映射
		pathMethodMappingMap.put(path, webSocketEndpointMethodMapping);
		for (MethodArgumentResolver onOpenArgResolver : webSocketEndpointMethodMapping.getOnOpenArgResolvers()) {
			if (onOpenArgResolver instanceof PathVariableMethodArgumentResolver
					|| onOpenArgResolver instanceof PathVariableMapMethodArgumentResolver) {
				pathMatchers.add(new AntPathMatcherWrapper(path));
				return;
			}
		}

		pathMatchers.add(new DefaultPathMatcher(path));
	}

	/**
	 * 获取端点方法映射
	 */
	private WebSocketEndpointMethodMapping getWebSocketMethodMapping(Channel channel) {
		Attribute<String> attrPath = channel.attr(PATH_KEY);
		if (pathMethodMappingMap.size() == 1) {
			return pathMethodMappingMap.values().iterator().next();
		}
		else {
			String path = attrPath.get();
			return pathMethodMappingMap.get(path);
		}
	}

	/**
	 * 根据路径获取端点方法映射
	 */
	private WebSocketEndpointMethodMapping getWebSocketMethodMapping(String path, Channel channel) {
		WebSocketEndpointMethodMapping methodMapping;
		if (pathMethodMappingMap.size() == 1) {
			methodMapping = pathMethodMappingMap.values().iterator().next();
		}
		else {
			Attribute<String> attrPath = channel.attr(PATH_KEY);
			attrPath.set(path);
			methodMapping = pathMethodMappingMap.get(path);
			if (methodMapping == null) {
				throw new RuntimeException("path " + path + " is not in pathMethodMappingMap ");
			}
		}
		return methodMapping;
	}

	public String getHost() {
		return config.getHost();
	}

	public int getPort() {
		return config.getPort();
	}

	public Set<WsPathMatcher> getPathMatcherSet() {
		return pathMatchers;
	}

}
