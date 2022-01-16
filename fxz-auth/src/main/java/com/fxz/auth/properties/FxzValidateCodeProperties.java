package com.fxz.auth.properties;

import lombok.Data;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 16:45
 */
@Data
public class FxzValidateCodeProperties {

	/**
	 * 验证码有效时间，单位秒
	 */
	private Long time = 12000L;

	/**
	 * 验证码类型，可选值 png和 gif
	 */
	private String type = "png";

	/**
	 * 图片宽度，px
	 */
	private Integer width = 130;

	/**
	 * 图片高度，px
	 */
	private Integer height = 48;

	/**
	 * 验证码位数
	 */
	private Integer length = 4;

	/**
	 * 验证码值的类型 1. 数字加字母 2. 纯数字 3. 纯字母
	 */
	private Integer charType = 2;

}
