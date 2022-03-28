package com.fxz.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-27 22:23
 */
@Controller
@RequestMapping("/token")
public class Page {

	/**
	 * 认证页面
	 * @param modelAndView
	 * @param error 表单登录失败处理回调的错误信息
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public String require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
		return "ftl/login";
	}

	/**
	 * 确认授权页面
	 * @param request
	 * @param session
	 * @param modelAndView
	 * @return
	 */
	@GetMapping("/confirm_access")
	public String confirm(HttpServletRequest request, HttpSession session, ModelAndView modelAndView) {
		/*
		 * Map<String, Object> scopeList = (Map<String, Object>)
		 * request.getAttribute("scopes"); modelAndView.addObject("scopeList",
		 * scopeList.keySet());
		 *
		 * Object auth = session.getAttribute("authorizationRequest"); if (auth != null) {
		 * AuthorizationRequest authorizationRequest = (AuthorizationRequest) auth;
		 * ClientDetails clientDetails =
		 * clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
		 * modelAndView.addObject("app", clientDetails.getAdditionalInformation());
		 * modelAndView.addObject("user", SecurityUtils.getUser()); }
		 */

		return "ftl/confirm";
	}

}
