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

package com.art.system.controller;

import com.art.common.core.result.Result;
import com.art.common.security.annotation.Ojbk;
import com.art.common.sms.service.AliyunSmsService;
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
 * @version 0.0.1
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
