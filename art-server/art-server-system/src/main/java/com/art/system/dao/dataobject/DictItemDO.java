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
import com.art.common.mp.core.base.MpEntity;
import lombok.Data;

/**
 * 字典项
 *
 * @author fxz
 * @date 2022-04-04
 */
@Data
@TableName("sys_dict_item")
public class DictItemDO extends MpEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 字典ID
	 */
	private Long dictId;

	/**
	 * 值
	 */
	private String value;

	/**
	 * 标签
	 */
	private String label;

	/**
	 * 字典类型
	 */
	private String type;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 排序（升序）
	 */
	private Integer sortOrder;

	/**
	 * 备注
	 */
	private String remark;

}
