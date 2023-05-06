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

package com.art.gen.dao.dataobject;

import com.art.common.mp.core.base.BaseCreateEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * 数据源表
 *
 * @author fxz
 * @date 2022-03-31
 */
@FieldNameConstants
@Data
@TableName("gen_datasource_conf")
public class DatasourceConfDO extends BaseCreateEntity {

	private static final long serialVersionUID = 666L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 数据源名称
	 */
	private String name;

	/**
	 * jdbc-url
	 */
	private String url;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 删除标记
	 */
	@TableLogic
	private String delFlag;

}
