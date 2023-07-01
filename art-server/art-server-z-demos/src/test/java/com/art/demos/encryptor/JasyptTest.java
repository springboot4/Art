/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.demos.encryptor;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Fxz
 * @version 0.0.1
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
