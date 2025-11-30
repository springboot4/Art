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

package com.art.gateway.handler;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 网关默认首页
 *
 * @author fxz
 */
@Setter
@ConfigurationProperties(prefix = "art")
@Component
public class GatewayIndexHandler implements HandlerFunction<ServerResponse> {

	private String name;

	private String author;

	private String version;

	@Override
	public Mono<ServerResponse> handle(ServerRequest request) {
		String sb = "<div style='color: red'>" + name + " gateway started!</div>" + "<br/>" + "author: " + author
				+ "<br/>" + "version: " + version + "<br/>" + "<div><ul>"
				+ "<li>Knife4j: <a href='doc.html' target='_blank'>Knife4j</a></li>"
				+ "<li>Document: <a href='javaai.art' target='_blank'>Document</a></li>" + "</ul></div>";

		return ServerResponse.status(HttpStatus.OK)
			.contentType(MediaType.valueOf(String.valueOf(MediaType.TEXT_HTML)))
			.body(BodyInserters.fromValue(sb));
	}

}