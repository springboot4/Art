
/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.database.core.constant;

/**
 * @author fxz
 * <p>
 * 数据源相关常量
 */
public interface DataSourceConstants {

	/**
	 * 数据源名称
	 */
	String DS_NAME = "name";

	/**
	 * 默认数据源（master）
	 */
	String DS_MASTER = "master";

	/**
	 * jdbcUrl
	 */
	String DS_JDBC_URL = "url";

	/**
	 * 数据库用户名
	 */
	String DS_USER_NAME = "username";

	/**
	 * 数据库密码
	 */
	String DS_USER_PWD = "password";

	/**
	 * 驱动包名称
	 */
	String DS_DRIVER_CLASS_NAME = "driver_class_name";

}
