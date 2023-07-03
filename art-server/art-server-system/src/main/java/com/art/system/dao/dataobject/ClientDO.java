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

package com.art.system.dao.dataobject;

import com.art.mybatis.common.base.BaseCreateEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 终端信息表
 *
 * @author fxz
 * @date 2023-04-13
 */
@Data
@TableName("sys_oauth_client_details")
public class ClientDO extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 客户端ID
	 */
	@TableId(type = IdType.INPUT)
	private String clientId;

	/**
	 * 资源列表
	 */
	private String resourceIds;

	/**
	 * 客户端密钥
	 */
	private String clientSecret;

	/**
	 * 域
	 */
	private String scope;

	/**
	 * 认证类型
	 */
	private String authorizedGrantTypes;

	/**
	 * 重定向地址
	 */
	private String webServerRedirectUri;

	/**
	 * 角色列表
	 */
	private String authorities;

	/**
	 * token 有效期
	 */
	private Integer accessTokenValidity;

	/**
	 * 刷新令牌有效期
	 */
	private Integer refreshTokenValidity;

	/**
	 * 令牌扩展字段JSON
	 */
	private String additionalInformation;

	/**
	 * 是否自动放行
	 */
	private String autoapprove;

}
