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

package com.art.common.security.authentication.gitee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class GiteeUserInfoResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("gists_url")
	private String gistsUrl;

	/**
	 * 仓库URL
	 */
	@JsonProperty("repos_url")
	private String reposUrl;

	@JsonProperty("following_url")
	private String followingUrl;

	/**
	 * 自我介绍
	 */
	private String bio;

	/**
	 * 创建时间
	 */
	@JsonProperty("created_at")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'+08:00'")
	private LocalDateTime createdAt;

	/**
	 * 企业备注名
	 */
	private String remark;

	/**
	 * 码云Gitee登录用户名
	 */
	private String login;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 博客地址
	 */
	private String blog;

	/**
	 * 订阅项目URL
	 */
	@JsonProperty("subscriptions_url")
	private String subscriptionsUrl;

	/**
	 * 微博地址
	 */
	private String weibo;

	/**
	 * 更新时间
	 */
	@JsonProperty("updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'+08:00'")
	private LocalDateTime updatedAt;

	/**
	 * 码云Gitee唯一标识
	 */
	private Integer id;

	/**
	 * 公共仓库数
	 */
	@JsonProperty("public_repos")
	private Integer publicRepos;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 组织URL
	 */
	@JsonProperty("organizations_url")
	private String organizationsUrl;

	/**
	 * star项目URL
	 */
	@JsonProperty("starred_url")
	private String starredUrl;

	/**
	 * 粉丝URL
	 */
	@JsonProperty("followers_url")
	private String followersUrl;

	@JsonProperty("public_gists")
	private Integer publicGists;

	/**
	 * 公开资料URL
	 */
	private String url;

	/**
	 * 接收事件
	 */
	@JsonProperty("received_events_url")
	private String receivedEventsUrl;

	/**
	 * 关注的仓库
	 */
	private Integer watched;

	/**
	 * 粉丝数
	 */
	private Integer followers;

	/**
	 * 头像
	 */
	@JsonProperty("avatar_url")
	private String avatarUrl;

	@JsonProperty("events_url")
	private String eventsUrl;

	/**
	 * 空间URL
	 */
	@JsonProperty("html_url")
	private String htmlUrl;

	/**
	 * 关注的人
	 */
	private Integer following;

	private String name;

	/**
	 * star数
	 */
	private Integer stared;

	/**
	 * 公司
	 *
	 * @see <a href="https://gitee.com/api/v5/swagger#/getV5UsersUsername">获取一个用户</a>
	 */
	private String company;

	/**
	 * 职务
	 *
	 * @see <a href="https://gitee.com/api/v5/swagger#/getV5UsersUsername">获取一个用户</a>
	 */
	private String profession;

	/**
	 * 微信
	 *
	 * @see <a href="https://gitee.com/api/v5/swagger#/getV5UsersUsername">获取一个用户</a>
	 */
	private String wechat;

	/**
	 * QQ
	 *
	 * @see <a href="https://gitee.com/api/v5/swagger#/getV5UsersUsername">获取一个用户</a>
	 */
	private String qq;

	/**
	 * 领英
	 *
	 * @see <a href="https://gitee.com/api/v5/swagger#/getV5UsersUsername">获取一个用户</a>
	 */
	private String linkedin;

	/**
	 * 异常消息
	 */
	private String message;

}
