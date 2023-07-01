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

import com.art.common.websocket.core.annotation.OnBinary;
import com.art.common.websocket.core.support.MethodArgumentResolver;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.springframework.core.MethodParameter;

import java.util.Objects;

/**
 * byte类型参数方法解析
 */
public class ByteMethodArgumentResolver implements MethodArgumentResolver {

	@Override
	public boolean support(MethodParameter parameter) {
		return Objects.requireNonNull(parameter.getMethod()).isAnnotationPresent(OnBinary.class)
				&& byte[].class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
		BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) object;
		ByteBuf content = binaryWebSocketFrame.content();
		byte[] bytes = new byte[content.readableBytes()];
		content.readBytes(bytes);
		return bytes;
	}

}