package com.fxz.system.controller;

import cn.hutool.extra.servlet.ServletUtil;
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

	/**
	 * 获取部门树
	 */
	@Ojbk
	@GetMapping("/ipTest")
	public Result<Object> getDeptTree(HttpServletRequest request) {
		String ip = ServletUtil.getClientIP(request);
		log.info("ip:{}", ip);
		return Result.success(ip);
	}

}
