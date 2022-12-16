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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件管理
 *
 * @author fxz
 * @date 2022-04-04
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@TableName("sys_file")
public class FileDO extends MpEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 桶名称
	 */
	private String bucketName;

	/**
	 * 原始文件名
	 */
	private String original;

	/**
	 * 文件类型
	 */
	private String type;

	/**
	 * 文件大小
	 */
	private Long fileSize;

}
