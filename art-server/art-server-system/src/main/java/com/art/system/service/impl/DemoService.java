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

package com.art.system.service.impl;

import com.art.system.dao.dataobject.SystemUserDO;
import com.art.system.dao.mysql.DemoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-03-30 10:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DemoService {

	private final DemoMapper demoMapper;

	public void queryDatabase() {
		List<Map<String, String>> slave = demoMapper.selectTest("test_slave", "slave");
		List<SystemUserDO> master = demoMapper.selectUser("sys_user", "master");
		log.info("slave:{}", slave);
		log.info("master:{}", master);
	}

}
