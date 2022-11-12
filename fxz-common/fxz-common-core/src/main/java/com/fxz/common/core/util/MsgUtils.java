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

package com.fxz.common.core.util;

import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * i18n 工具类 默认中文，可以通过LocaleContextHolder.getLocale() 动态设置语言环境
 *
 * @author fxz
 */
@UtilityClass
public class MsgUtils {

	/**
	 * 通过code 获取中文错误信息
	 * @param code 错误编码
	 * @return 错误信息
	 */
	public String getMessage(String code) {
		MessageSource messageSource = SpringUtil.getBean("messageSource");
		return messageSource.getMessage(code, null, Locale.CHINA);
	}

	/**
	 * 通过code 和参数获取中文错误信息
	 * @param code 错误编码
	 * @param objects 参数
	 * @return 错误信息
	 */
	public String getMessage(String code, Object... objects) {
		MessageSource messageSource = SpringUtil.getBean("messageSource");
		return messageSource.getMessage(code, objects, Locale.CHINA);
	}

}
