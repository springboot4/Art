/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.common.component;

import cn.hutool.core.map.MapUtil;
import com.common.constant.SecurityConstants;
import com.common.entity.FxzAuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author fxz
 * <p>
 * 根据checktoken 的结果转化用户信息
 * <p>
 * 问题分析: 认证服务器check-token 返回的全部信息 资源服务器在根据返回信息组装用户信息的时候，只是用了username 如果设置了
 * userDetailsService 的实现则去调用 loadUserByUsername 再去查询一次用户信息
 * <p>
 * 造成问题现象 如果设置了userDetailsService 即可在spring security 上下文获取用户的全部信息，不设置则只能得到用户名。
 * 增加了一次查询逻辑，对性能产生不必要的影响
 * <p>
 * 解决问题 扩展UserAuthenticationConverter 的解析过程，把认证服务器返回的信息全部组装到spring security的上下文对象中
 */
@Slf4j
public class FxzUserAuthenticationConverter implements UserAuthenticationConverter {

    private static final String N_A = "N/A";

    /**
     * Extract information about the user to be used in an access token (i.e. for resource
     * servers).
     *
     * @param authentication an authentication representing a user
     * @return a map of key values representing the unique information about the user
     */
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(USERNAME, authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    /**
     * Inverse of {@link #convertUserAuthentication(Authentication)}. Extracts an
     * Authentication from a map.
     *
     * @param responseMap a map of user information
     * @return an Authentication representing the user or null if there is none
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> responseMap) {
        if (responseMap.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = getAuthorities(responseMap);
            Map<String, ?> map = MapUtil.get(responseMap, SecurityConstants.DETAILS_USER, Map.class);
            String password = MapUtil.getStr(map, SecurityConstants.PASSWORD);
            String username = MapUtil.getStr(map, SecurityConstants.DETAILS_USERNAME);
            FxzAuthUser user = new FxzAuthUser(username, password, true, true, true, true, authorities);
            return new UsernamePasswordAuthenticationToken(user, N_A, authorities);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(
                    StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        return AuthorityUtils.NO_AUTHORITIES;
    }


}
