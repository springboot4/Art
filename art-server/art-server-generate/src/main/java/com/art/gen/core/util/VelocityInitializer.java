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

package com.art.gen.core.util;

import org.apache.velocity.app.Velocity;

import java.util.Properties;

/**
 * VelocityEngine工厂
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022-03-03 15:44
 */
public class VelocityInitializer {

	/**
	 * 初始化vm方法
	 */
	public static void initVelocity() {
		try {
			// 加载classpath目录下的vm文件
			Properties prop = new Properties();
			prop.put("resource.loader.file.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			// 初始化Velocity引擎，指定配置Properties
			Velocity.init(prop);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
