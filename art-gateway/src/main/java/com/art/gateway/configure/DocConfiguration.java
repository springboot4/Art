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

package com.art.gateway.configure;

import com.art.gateway.handler.GatewayIndexHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/22 23:16
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class DocConfiguration {

	@Bean
	public RouterFunction<ServerResponse> docIndexHandler(GatewayIndexHandler gatewayIndexHandler) {
		return RouterFunctions.route(RequestPredicates.GET("/").and(RequestPredicates.accept(MediaType.ALL)),
				gatewayIndexHandler);
	}

}
