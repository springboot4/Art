package com.fxz.gen.util;

import org.apache.velocity.app.Velocity;

import java.util.Properties;

/**
 * VelocityEngine工厂
 *
 * @author Fxz
 * @version 1.0
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
