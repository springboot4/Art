package com.fxz.auth.service;

import cn.hutool.core.util.ObjectUtil;
import com.fxz.auth.manager.UserManager;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.mall.user.entity.Member;
import com.fxz.mall.user.feign.RemoteMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 商城会员用户认证服务
 *
 * @author fxz
 */
@Slf4j
@RequiredArgsConstructor
@Service("fxzMemberUserDetailsServiceImpl")
public class FxzMemberUserDetailsServiceImpl implements UserDetailsService {

	private final UserManager userManager;

	public UserDetails loadUserByUsername(String username) {
		return null;
	}

	/**
	 * 手机号码认证方式
	 * @param mobile
	 * @return
	 */
	public UserDetails loadUserByMobile(String mobile) {
		// todo
		return null;
	}

	/**
	 * openid 认证方式
	 * @param openId
	 * @return
	 */
	public UserDetails loadUserByOpenId(String openId) {
		// todo
		return null;
	}

}
