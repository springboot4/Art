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

package com.art.system.api.client.dto;

import com.art.common.mp.core.base.BaseCreateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 终端信息表
 *
 * @author fxz
 * @date 2023-04-13
 */
@Schema(title = "终端信息表")
@Data
public class ClientDetailsDTO extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	@Schema(description = "客户端ID")
	private String clientId;

	@Schema(description = "资源列表")
	private String resourceIds;

	@Schema(description = "客户端密钥")
	private String clientSecret;

	@Schema(description = "域")
	private String scope;

	@Schema(description = "认证类型")
	private String authorizedGrantTypes;

	@Schema(description = "重定向地址")
	private String webServerRedirectUri;

	@Schema(description = "角色列表")
	private String authorities;

	@Schema(description = "token 有效期")
	private Integer accessTokenValidity;

	@Schema(description = "刷新令牌有效期")
	private Integer refreshTokenValidity;

	@Schema(description = "令牌扩展字段JSON")
	private String additionalInformation;

	@Schema(description = "是否自动放行")
	private String autoapprove;

}