package com.fxz.auth.service;

import cn.hutool.core.util.ObjectUtil;
import com.fxz.auth.manager.UserManager;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.system.entity.SystemUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 16:18
 */
@Service
@RequiredArgsConstructor
public class FxzUserDetailServiceImpl implements UserDetailsService {

	private final UserManager userManager;

	/**
	 * 通过用户名从数据库中获取用户信息SystemUser和用户权限集合
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SystemUser systemUser = userManager.findByName(username);
		if (ObjectUtil.isNotEmpty(systemUser)) {
			String permissions = userManager.findUserPermissions(systemUser.getUsername());
			boolean notLocked = false;
			if (StringUtils.equals(SystemUser.STATUS_VALID, systemUser.getStatus()))
				notLocked = true;
			FxzAuthUser authUser = new FxzAuthUser(systemUser.getUsername(), systemUser.getPassword(), true, true, true,
					notLocked, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

			BeanUtils.copyProperties(systemUser, authUser);
			return authUser;
		}
		else {
			throw new UsernameNotFoundException("");
		}
	}

}
