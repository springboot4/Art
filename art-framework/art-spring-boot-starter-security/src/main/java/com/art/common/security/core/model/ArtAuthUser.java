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

package com.art.common.security.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-27 16:23
 */
@Accessors(chain = true)
@Getter
@Setter
public class ArtAuthUser extends User implements OAuth2AuthenticatedPrincipal {

	private static final long serialVersionUID = -6819532868284272852L;

	private Long userId;

	private String avatar;

	private String email;

	private String mobile;

	private String sex;

	private Long deptId;

	private String deptName;

	private String roleId;

	private String roleName;

	private Date lastLoginTime;

	private String description;

	private String status;

	private Long tenantId;

	private final Map<String, Object> attributes = new HashMap<>();

	public ArtAuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public ArtAuthUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return
	 */
	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	/**
	 * @return
	 */
	@Override
	public String getName() {
		return this.getUsername();
	}

}
