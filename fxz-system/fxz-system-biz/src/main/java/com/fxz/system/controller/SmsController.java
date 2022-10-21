package com.fxz.system.controller;

import com.fxz.common.mp.result.Result;
import com.fxz.common.security.annotation.Ojbk;
import com.fxz.common.sms.service.AliyunSmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信管理
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/5/19 21:04
 */
@Tag(name = "短信管理")
@Slf4j
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {

	private final AliyunSmsService aliyunSmsService;

	/**
	 * 发送短信
	 * @param phoneNumber 手机号
	 */
	@Operation(summary = "发送短信")
	@Ojbk
	@GetMapping("/sendSms")
	public <T> Result<T> sendSmsCode(String phoneNumber) {
		return Result.judge(aliyunSmsService.sendSmsCode(phoneNumber));
	}

}
