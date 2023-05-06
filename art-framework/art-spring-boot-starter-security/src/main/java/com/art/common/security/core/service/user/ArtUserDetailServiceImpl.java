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

package com.art.common.security.core.service.user;

import com.art.common.core.constant.SecurityConstants;
import com.art.common.security.authentication.sms.OAuth2ResourceOwnerSmsAuthenticationConverter;
import com.art.common.security.core.model.ArtAuthUser;
import com.art.common.security.core.service.ArtUserDetailsService;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.system.api.user.dto.SystemUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-27 16:18
 */
@Slf4j
@Service("artUserDetailServiceImpl")
@RequiredArgsConstructor
public class ArtUserDetailServiceImpl implements ArtUserDetailsService {

	private final ArtUserManager artUserManager;

	/**
	 * 通过用户名从数据库中获取用户信息SystemUser和用户权限集合
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (OAuth2ResourceOwnerSmsAuthenticationConverter.SMS.getValue().equals(SecurityUtil.getGrantType())) {
			// 认证方式通过用户名 手机号 认证
			return this.loadUserByMobile(username);
		}
		else {
			// 认证方式通过用户名 username 认证
			SystemUserDTO userDTO = artUserManager.findByName(username);
			UserDetails userDetails = getUserDetails(userDTO);
			return userDetails;
		}
	}

	@Override
	public UserDetails loadUserByMobile(String mobile) {
		return getUserDetails(artUserManager.findByMobile(mobile));
	}

	private UserDetails getUserDetails(SystemUserDTO systemUser) {
		if (Objects.nonNull(systemUser)) {
			String permissions = artUserManager.findUserPermissions(systemUser.getUsername());

			boolean notLocked = StringUtils.equals(SystemUserDTO.STATUS_VALID, systemUser.getStatus());

			ArtAuthUser authUser = new ArtAuthUser(systemUser.getUsername(), systemUser.getPassword(), true, true, true,
					notLocked, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

			BeanUtils.copyProperties(systemUser, authUser);
			return authUser;
		}

		return null;
	}

	/**
	 * 是否支持此客户端校验
	 * @param clientId 目标客户端
	 * @return true/false
	 */
	@Override
	public boolean support(String clientId) {
		return SecurityConstants.ADMIN_CLIENT_ID.equals(clientId);
	}

}
