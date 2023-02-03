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
import com.art.common.mp.core.base.BaseEntity;
import com.art.common.mp.core.encrypt.core.annotation.EncryptionData;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 操作日志记录
 *
 * @author fxz
 * @date 2022-04-07
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@FieldNameConstants
@TableName("sys_oper_log")
public class OperLogDO extends BaseEntity {

	/**
	 * 模块标题
	 */
	private String title;

	/**
	 * 业务类型
	 */
	private Integer businessType;

	/**
	 * 方法名称
	 */
	private String method;

	/**
	 * 请求方式
	 */
	private String requestMethod;

	/**
	 * 操作人员
	 */
	private String operName;

	/**
	 * 请求URL
	 */
	private String operUrl;

	/**
	 * 主机地址
	 */
	private String operIp;

	/**
	 * 请求参数
	 */
	@EncryptionData
	private String operParam;

	/**
	 * 操作状态（0正常 1异常）
	 */
	private Integer status;

	/**
	 * 错误消息
	 */
	private String errorMsg;

	/**
	 * 执行时间
	 */
	private Long time;

}
