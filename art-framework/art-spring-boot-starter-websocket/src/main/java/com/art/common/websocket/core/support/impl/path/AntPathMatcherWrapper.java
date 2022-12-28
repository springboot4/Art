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
