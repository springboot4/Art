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

package com.art.gateway.component;

import com.art.common.core.constant.CodeTypeEnum;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * @author fxz
 */
@Component
public class EasyCaptchaProducer {

	/**
	 * 验证码内容长度
	 */
	private final int length = 4;

	/**
	 * 验证码宽度
	 */
	private final int width = 120;

	/**
	 * 验证码高度
	 */
	private final int height = 36;

	/**
	 * 验证码字体
	 */
	private final String fontName = "Verdana";

	/**
	 * 字体风格
	 */
	private final Integer fontStyle = Font.PLAIN;

	/**
	 * 字体大小
	 */
	private final int fontSize = 20;

	public Captcha getCaptcha(CodeTypeEnum codeType) {
		Captcha captcha;
		switch (codeType) {
			case ARITHMETIC:
				// 算术类型
				captcha = new FixedArithmeticCaptcha(width, height);
				// 固定设置为两位，图片为算数运算表达式
				captcha.setLen(2);
				break;
			case CHINESE:
				captcha = new ChineseCaptcha(width, height);
				captcha.setLen(length);
				break;
			case CHINESE_GIF:
				captcha = new ChineseGifCaptcha(width, height);
				captcha.setLen(length);
				break;
			case GIF:
				// 最后一位是位数
				captcha = new GifCaptcha(width, height);
				captcha.setLen(length);
				break;
			case SPEC:
				captcha = new SpecCaptcha(width, height);
				captcha.setLen(length);
				break;
			default:
				throw new RuntimeException("验证码配置信息错误！");
		}
		captcha.setFont(new Font(fontName, fontStyle, fontSize));
		return captcha;
	}

	static class FixedArithmeticCaptcha extends ArithmeticCaptcha {

		public FixedArithmeticCaptcha(int width, int height) {
			super(width, height);
		}

		@Override
		protected char[] alphas() {
			// 生成随机数字和运算符
			int n1 = num(1, 10), n2 = num(1, 10);
			int opt = num(3);

			// 计算结果
			int res = new int[] { n1 + n2, n1 - n2, n1 * n2 }[opt];
			// 转换为字符运算符
			char optChar = "+-x".charAt(opt);

			this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
			this.chars = String.valueOf(res);

			return chars.toCharArray();
		}

	}

}
