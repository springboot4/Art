/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fxz.gateway.router;

import com.fxz.gateway.handler.AjImageCodeCheckHandler;
import com.fxz.gateway.handler.AjImageCodeCreateHandler;
import com.fxz.gateway.handler.EasyCaptchaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 验证码路由
 *
 * @author fxz
 */
@Configuration
public class CaptchaRouter {

	@Bean
	public RouterFunction<ServerResponse> captchaRouterFunction(EasyCaptchaHandler easyCaptchaHandler,
			AjImageCodeCreateHandler ajImageCodeCreateHandler, AjImageCodeCheckHandler ajImageCodeCheckHandler) {
		return RouterFunctions
				.route(RequestPredicates.GET("/captcha").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
						easyCaptchaHandler)
				.andRoute(RequestPredicates.path("/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
						ajImageCodeCreateHandler)
				.andRoute(RequestPredicates.POST("/code/check").and(RequestPredicates.accept(MediaType.ALL)),
						ajImageCodeCheckHandler);
	}

}
