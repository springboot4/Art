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

package com.art.common.captcha.properties;

import com.anji.captcha.model.common.CaptchaTypeEnum;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.awt.*;

/**
 * @author fxz
 */
@ToString
@Data
@ConfigurationProperties(AjCaptchaProperties.PREFIX)
public class AjCaptchaProperties {

	public static final String PREFIX = "aj.captcha";

	/**
	 * 验证码类型.
	 */
	private CaptchaTypeEnum type = CaptchaTypeEnum.DEFAULT;

	/**
	 * 滑动拼图底图路径.
	 */
	private String jigsaw = "classpath:images/jigsaw";

	/**
	 * 点选文字底图路径.
	 */
	private String picClick = "classpath:images/pic-click";

	/**
	 * 右下角水印文字(我的水印).
	 */
	private String waterMark = "Art";

	/**
	 * 右下角水印字体(文泉驿正黑).
	 */
	private String waterFont = "WenQuanZhengHei.ttf";

	/**
	 * 点选文字验证码的文字字体(文泉驿正黑).
	 */
	private String fontType = "WenQuanZhengHei.ttf";

	/**
	 * 校验滑动拼图允许误差偏移量(默认5像素).
	 */
	private String slipOffset = "5";

	/**
	 * aes加密坐标开启或者禁用(true|false).
	 */
	private Boolean aesStatus = true;

	/**
	 * 滑块干扰项(0/1/2)
	 */
	private String interferenceOptions = "0";

	/**
	 * local缓存的阈值
	 */
	private String cacheNumber = "1000";

	/**
	 * 定时清理过期local缓存(单位秒)
	 */
	private String timingClear = "180";

	/**
	 * 缓存类型
	 */
	private StorageType cacheType = StorageType.local;

	/**
	 * 历史数据清除开关
	 */
	private boolean historyDataClearEnable = false;

	/**
	 * 一分钟内接口请求次数限制 开关
	 */
	private boolean reqFrequencyLimitEnable = false;

	/**
	 * 一分钟内check接口失败次数
	 */
	private int reqGetLockLimit = 5;

	/**
	 * 验证失败后，锁定时间间隔
	 */
	private int reqGetLockSeconds = 300;

	/**
	 * get接口一分钟内限制访问数
	 */
	private int reqGetMinuteLimit = 100;

	/**
	 * check接口一分钟内请求数限制
	 */
	private int reqCheckMinuteLimit = 100;

	/**
	 * verify接口一分钟内请求数限制
	 */
	private int reqVerifyMinuteLimit = 100;

	/**
	 * 点选字体样式
	 */
	private int fontStyle = Font.BOLD;

	/**
	 * 点选字体大小
	 */
	private int fontSize = 25;

	/**
	 * 点选文字个数，存在问题，暂不要使用
	 */
	private int clickWordCount = 4;

	public enum StorageType {

		/**
		 * 内存.
		 */
		local,
		/**
		 * redis.
		 */
		redis,
		/**
		 * 其他.
		 */
		other

	}

}
