package com.fxz.system.controller;

import com.fxz.system.entity.TradeLog;
import com.fxz.system.service.ITradeLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author fxz
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

	private final ITradeLogService tradeLogService;

	@GetMapping("/pay")
	public void orderAndPay(TradeLog tradeLog) {
		this.tradeLogService.orderAndPay(tradeLog);
	}

	@GetMapping("/info")
	public String test() {
		return "fxz-server-system";
	}

	@GetMapping("/currentUser")
	public Principal currentUser(Principal principal) {
		return principal;
	}

	@GetMapping("/hello")
	public String hello(String name) {
		log.info("/hello服务被调用");
		return "hello" + name;
	}

}