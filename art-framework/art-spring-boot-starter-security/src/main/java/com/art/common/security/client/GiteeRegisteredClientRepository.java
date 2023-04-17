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

package com.art.common.security.client;

import com.art.common.security.authentication.gitee.GiteeTokenResponse;
import com.art.common.security.authentication.gitee.GiteeUserInfoResponse;
import com.art.common.security.client.properties.GiteeProperties;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/16 11:42
 */
public interface GiteeRegisteredClientRepository {

	/**
	 * 根据 appid 获取重定向的地址
	 * @param appid 码云Gitee client_id
	 * @return 返回重定向的地址
	 * @throws OAuth2AuthenticationException OAuth 2.1 可处理的异常，可使用
	 * {@link OAuth2AuthorizationServerConfigurer#tokenEndpoint(Customizer)} 中的
	 * {@link OAuth2TokenEndpointConfigurer#errorResponseHandler(AuthenticationFailureHandler)}
	 * 拦截处理此异常
	 */
	String getRedirectUriByAppid(String appid);

	/**
	 * 生成状态码
	 * @param appid 码云应用id
	 * @return 返回生成的授权码
	 */
	String stateGenerate(String appid);

	/**
	 * @param appid 开放平台 网站应用 ID
	 * @param id 码云用户唯一标识
	 * @param userId 系统用户id
	 */
	void storeBinding(Integer id, String appid, Long userId);

	/**
	 * 储存gitee用户
	 * @param giteeUserInfoResponse gitee用户信息
	 * @param appid 码云网站应用ID
	 */
	void storeGiteeUsers(GiteeTokenResponse giteeTokenResponse, GiteeUserInfoResponse giteeUserInfoResponse,
			String appid);

	/**
	 * 状态码验证（返回 {@link Boolean#FALSE} 时，将终止后面需要执行的代码）
	 * @param appid 开放平台 网站应用 ID
	 * @param code 授权码
	 * @param state 状态码
	 * @return 返回 状态码验证结果
	 */
	boolean stateValid(String appid, String code, String state);

	/**
	 * 根据 appid 获取 码云Gitee属性配置
	 * @param appid 码云Gitee client_id
	 * @return 返回 码云Gitee属性配置
	 * @throws OAuth2AuthenticationException OAuth 2.1 可处理的异常，可使用
	 * {@link OAuth2AuthorizationServerConfigurer#tokenEndpoint(Customizer)} 中的
	 * {@link OAuth2TokenEndpointConfigurer#errorResponseHandler(AuthenticationFailureHandler)}
	 * 拦截处理此异常
	 */
	GiteeProperties.Gitee getGiteeByAppid(String appid) throws OAuth2AuthenticationException;

	/**
	 * 根据 AppID(码云Gitee client_id)、code、jsCode2SessionUrl 获取Token
	 * @param appid AppID(码云Gitee
	 * client_id)，<a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @param code 授权码，<a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @param accessTokenUrl <a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @return 返回 码云Gitee授权结果
	 * @throws OAuth2AuthenticationException OAuth 2.1 可处理的异常，可使用
	 * {@link OAuth2AuthorizationServerConfigurer#tokenEndpoint(Customizer)} 中的
	 * {@link OAuth2TokenEndpointConfigurer#errorResponseHandler(AuthenticationFailureHandler)}
	 * 拦截处理此异常
	 */
	GiteeTokenResponse getAccessTokenResponse(String appid, String code, String accessTokenUrl)
			throws OAuth2AuthenticationException;

	/**
	 * 获取授权用户的资料
	 * @param userinfoUrl 用户信息接口
	 * @param appid AppID(码云Gitee client_id)
	 * @param state 状态码
	 * @param binding 是否绑定，需要使用者自己去拓展
	 * @param remoteAddress 用户IP
	 * @param sessionId SessionID
	 * @param giteeTokenResponse 码云 Token
	 * @return 返回授权用户的资料
	 * @see <a href="https://gitee.com/api/v5/swagger#/getV5User">获取授权用户的资料</a>
	 */
	GiteeUserInfoResponse getUserInfo(String userinfoUrl, String appid, String state,
			@NonNull GiteeTokenResponse giteeTokenResponse);

	/**
	 * 获取登录系统的用户信息
	 * @param appid 码云应用id
	 * @param id 码云用户唯一标识
	 * @return 系统用户信息
	 */
	UserDetails getUser(String appid, Integer id);

	/**
	 * 标记此次操作用户
	 * @param appid
	 * @param state
	 * @param userId
	 */
	void tagUsers(String appid, String state, Long userId);

	/**
	 * 获取此次操作用户
	 * @param appid
	 * @param state
	 * @return
	 */
	String getTagUser(String appid, String state);

}
