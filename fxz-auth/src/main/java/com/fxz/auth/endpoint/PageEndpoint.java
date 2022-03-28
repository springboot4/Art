package com.fxz.auth.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-27 22:23
 */
@Controller
@RequestMapping("/token")
public class PageEndpoint {

	/**
	 * 认证页面
	 */
	@GetMapping("/login")
	public String require() {
		return "ftl/login";
	}

	/**
	 * 确认授权页面
	 */
	@GetMapping("/confirm_access")
	public String confirm() {
		return "ftl/confirm";
	}

}
