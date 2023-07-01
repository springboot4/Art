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

package com.art.common.websocket.core.support.impl.path;

import com.art.common.websocket.core.support.WsPathMatcher;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.springframework.util.AntPathMatcher;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.art.common.websocket.core.support.WebSocketEndpointEventServer.URI_TEMPLATE;

public class AntPathMatcherWrapper extends AntPathMatcher implements WsPathMatcher {

	private final String pattern;

	public AntPathMatcherWrapper(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String getPattern() {
		return this.pattern;
	}

	@Override
	public boolean matchAndExtract(QueryStringDecoder decoder, Channel channel) {
		Map<String, String> variables = new LinkedHashMap<>();
		boolean result = doMatch(pattern, decoder.path(), true, variables);
		if (result) {
			channel.attr(URI_TEMPLATE).set(variables);
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AntPathMatcherWrapper that = (AntPathMatcherWrapper) o;
		return Objects.equals(pattern, that.pattern);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pattern);
	}

}
