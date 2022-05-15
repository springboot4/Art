package com.fxz.auth.service;

import cn.hutool.core.util.ObjectUtil;
import com.fxz.auth.manager.UserManager;
import com.fxz.common.core.constant.SecurityConstants;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.common.security.util.SecurityUtil;
import com.fxz.mall.user.entity.Member;
import com.fxz.system.entity.SystemUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
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
@Slf4j
@Primary
@Service("fxzUserDetailServiceImpl")
@RequiredArgsConstructor
public class FxzUserDetailServiceImpl implements UserDetailsService {

	private final UserManager userManager;

	/**
	 * 通过用户名从数据库中获取用户信息SystemUser和用户权限集合
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// todo 根据客户端id进行不同的feign调用查询用户信息，理论上移动端不需要用户名密码模式
		String clientId = SecurityUtil.getOAuth2ClientId();
		log.info("clientId:{}", clientId);
		if (clientId.equals(SecurityConstants.ADMIN_CLIENT_ID)) {
			log.info("系统端接口");
			SystemUser systemUser = userManager.findByName(username);
			if (ObjectUtil.isNotEmpty(systemUser)) {
				String permissions = userManager.findUserPermissions(systemUser.getUsername());
				boolean notLocked = false;
				if (StringUtils.equals(SystemUser.STATUS_VALID, systemUser.getStatus()))
					notLocked = true;
				FxzAuthUser authUser = new FxzAuthUser(systemUser.getUsername(), systemUser.getPassword(), true, true,
						true, notLocked, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

				BeanUtils.copyProperties(systemUser, authUser);
				return authUser;
			}

		}
		else {
			// todo
			log.info("会员接口");
			Member systemUser = userManager.loadUser(username);
			if (ObjectUtil.isNotEmpty(systemUser)) {
				FxzAuthUser authUser = new FxzAuthUser(systemUser.getNickName(), systemUser.getPassword(), true, true,
						true, true, AuthorityUtils.commaSeparatedStringToAuthorityList(null));

				BeanUtils.copyProperties(systemUser, authUser);

				return authUser;
			}
			else {
				throw new UsernameNotFoundException("");
			}
		}
		return null;
	}

}
