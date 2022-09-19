package com.fxz.gateway.handler;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.fxz.common.mp.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author fxz
 */
@Component
@RequiredArgsConstructor
public class AjImageCodeCreateHandler implements HandlerFunction<ServerResponse> {

	private final CaptchaService captchaService;

	@Override
	@SneakyThrows
	public Mono<ServerResponse> handle(ServerRequest serverRequest) {
		CaptchaVO vo = new CaptchaVO();
		vo.setCaptchaType("blockPuzzle");

		ResponseModel responseModel = captchaService.get(vo);

		return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(Result.success(responseModel)));
	}

}
