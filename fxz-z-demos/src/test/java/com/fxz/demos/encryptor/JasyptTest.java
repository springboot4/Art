package com.fxz.demos.encryptor;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/8/30 21:14
 */
@Slf4j
@SpringBootTest
public class JasyptTest {

	@Autowired
	private StringEncryptor stringEncryptor;

	@Test
	public void encryptor() {
		String redis = stringEncryptor.encrypt("redis");
		String mysql = stringEncryptor.encrypt("mysql");
		String appid = stringEncryptor.encrypt("appid");
		String secret = stringEncryptor.encrypt("secret");
		log.info("redis:{} ", redis);
		log.info("mysql:{} ", mysql);
		log.info("appid:{} ", appid);
		log.info("secret:{} ", secret);
	}

}
