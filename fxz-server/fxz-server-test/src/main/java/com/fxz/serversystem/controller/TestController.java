package com.fxz.serversystem.controller;

import com.fxz.common.core.entity.system.TradeLog;
import com.fxz.serversystem.service.IHelloService;
import com.fxz.serversystem.service.ITestTradeLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author fxz
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

	private final IHelloService helloService;

	private final ITestTradeLogService testTradeLogService;

	@PostMapping("/package/send")
	public void packageAndSend(@RequestBody TradeLog tradeLog) {
		testTradeLogService.packageAndSend(tradeLog);
	}

	@GetMapping("/hello")
	public String hello(String name) {
		log.info("Feign调用fxz-server-system的/hello服务");
		return this.helloService.hello(name);
	}

	@GetMapping("test1")
	@PreAuthorize("hasAnyAuthority('user:add')")
	public String test1() {
		return "拥有'user:add'权限";
	}

	@GetMapping("test2")
	@PreAuthorize("hasAnyAuthority('user:update')")
	public String test2() {
		return "拥有'user:update'权限";
	}

	@GetMapping("user")
	public Principal currentUser(Principal principal) {
		return principal;
	}

}