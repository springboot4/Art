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

package com.art.system.dao.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.art.common.mp.base.MpEntity;
import lombok.Data;

/**
 * 字典表
 *
 * @author fxz
 * @date 2022-04-04
 */
@Data
@TableName("sys_dict")
public class DictDO extends MpEntity {

	private static final long serialVersionUID = -7407384936260772014L;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 是否是系统内置
	 */
	private String systemFlag;

}
