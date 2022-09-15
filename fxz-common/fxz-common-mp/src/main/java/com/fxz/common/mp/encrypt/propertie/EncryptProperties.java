package com.fxz.common.mp.encrypt.propertie;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/15 14:05
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "fxz.common.encrypt")
public class EncryptProperties {

	/**
	 * 是否开启字段加密
	 */
	private boolean enableFieldDecrypt = true;

	/**
	 * aes秘钥
	 */
	private String fieldDecryptKey = "0123456789ABHAEQ";

}
