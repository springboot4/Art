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

package com.art.system.api.gitee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 码云Gitee用户表
 *
 * @author fxz
 * @date 2023-04-16
 */
@Schema(title = "码云Gitee用户表")
@Data
public class UsersGiteeDTO {

	private static final long serialVersionUID = -1L;

	@Schema(description = "码云Gitee用户表主键，自增")
	private Long usersGiteeId;

	@Schema(description = "绑定的用户主键，唯一键：uk__users_gitee__appid__id__users_id")
	private Long usersId;

	@Schema(description = "AppID(码云Gitee client_id)，唯一键：uk__users_gitee__appid__id")
	private String appid;

	@Schema(description = "码云Gitee唯一标识，不为空，唯一键：uk__users_gitee__appid__id")
	private Integer id;

	@Schema(description = "码云Gitee登录用户名，不为空")
	private String login;

	@Schema(description = "码云Gitee用户名，不为空")
	private String name;

	@Schema(description = "头像")
	private String avatarUrl;

	@Schema(description = "公开资料URL")
	private String url;

	@Schema(description = "空间URL")
	private String htmlUrl;

	@Schema(description = "企业备注名")
	private String remark;

	@Schema(description = "粉丝URL")
	private String followersUrl;

	@Schema(description = "")
	private String followingUrl;

	@Schema(description = "")
	private String gistsUrl;

	@Schema(description = "star项目URL")
	private String starredUrl;

	@Schema(description = "订阅项目URL")
	private String subscriptionsUrl;

	@Schema(description = "组织URL")
	private String organizationsUrl;

	@Schema(description = "仓库URL")
	private String reposUrl;

	@Schema(description = "")
	private String eventsUrl;

	@Schema(description = "接收事件")
	private String receivedEventsUrl;

	@Schema(description = "类型")
	private String type;

	@Schema(description = "博客地址")
	private String blog;

	@Schema(description = "微博地址")
	private String weibo;

	@Schema(description = "自我介绍")
	private String bio;

	@Schema(description = "公共仓库数")
	private Integer publicRepos;

	@Schema(description = "")
	private Integer publicGists;

	@Schema(description = "粉丝数")
	private Integer followers;

	@Schema(description = "关注的人")
	private Integer following;

	@Schema(description = "star数")
	private Integer stared;

	@Schema(description = "关注的仓库")
	private Integer watched;

	@Schema(description = "创建时间")
	private LocalDateTime createdAt;

	@Schema(description = "更新时间")
	private LocalDateTime updatedAt;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "绑定时间")
	private LocalDateTime bindingDate;

	@Schema(description = "授权凭证")
	private String accessToken;

	@Schema(description = "刷新凭证")
	private String refreshToken;

	@Schema(description = "过期时间")
	private LocalDateTime expires;

	@Schema(description = "授权范围")
	private String scope;

	@Schema(description = "公司")
	private String company;

	@Schema(description = "职务")
	private String profession;

	@Schema(description = "微信")
	private String wechat;

	@Schema(description = "QQ")
	private String qq;

	@Schema(description = "领英账户")
	private String linkedin;

}