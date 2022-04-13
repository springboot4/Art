package com.fxz.system.controller;

import cn.hutool.extra.servlet.ServletUtil;
import com.fxz.common.Idempotent.annotation.Idempotent;
import com.fxz.common.Idempotent.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.fxz.common.mp.result.Result;
import com.fxz.common.security.annotation.Ojbk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 18:33
 */
@Slf4j
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

	@Ojbk
	@GetMapping("/ipTest")
	public Result<Object> getDeptTree(HttpServletRequest request) {
		String ip = ServletUtil.getClientIP(request);
		log.info("ip:{}", ip);
		return Result.success(ip);
	}

	@Idempotent(timeout = 10, message = "别发请求，等我执行完", keyResolver = ExpressionIdempotentKeyResolver.class,
			keyArg = "#str")
	@Ojbk
	@GetMapping("/idempotent")
	public Result<Void> testIdempotent(String str) {
		log.info("方法执行");
		return Result.success();
	}

	@GetMapping("/authTest")
	public Result<Void> authTest() {
		log.info("authTest.....");
		return Result.success();
	}

}
