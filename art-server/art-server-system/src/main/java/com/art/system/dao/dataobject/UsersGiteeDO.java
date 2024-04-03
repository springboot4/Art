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

package com.art.system.dao.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 码云Gitee用户表
 *
 * @author fxz
 * @date 2023-04-16
 */
@Data
@TableName("sys_users_gitee")
public class UsersGiteeDO {

	private static final long serialVersionUID = -1L;

	/**
	 * 码云Gitee用户表主键，自增
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long usersGiteeId;

	/**
	 * 绑定的用户主键，唯一键：uk__users_gitee__appid__id__users_id
	 */
	private Long usersId;

	/**
	 * AppID(码云Gitee client_id)，唯一键：uk__users_gitee__appid__id
	 */
	private String appid;

	/**
	 * 码云Gitee唯一标识，不为空，唯一键：uk__users_gitee__appid__id
	 */
	private Integer id;

	/**
	 * 码云Gitee登录用户名，不为空
	 */
	private String login;

	/**
	 * 码云Gitee用户名，不为空
	 */
	private String name;

	/**
	 * 头像
	 */
	private String avatarUrl;

	/**
	 * 公开资料URL
	 */
	private String url;

	/**
	 * 空间URL
	 */
	private String htmlUrl;

	/**
	 * 企业备注名
	 */
	private String remark;

	/**
	 * 粉丝URL
	 */
	private String followersUrl;

	/**
	 *
	 */
	private String followingUrl;

	/**
	 *
	 */
	private String gistsUrl;

	/**
	 * star项目URL
	 */
	private String starredUrl;

	/**
	 * 订阅项目URL
	 */
	private String subscriptionsUrl;

	/**
	 * 组织URL
	 */
	private String organizationsUrl;

	/**
	 * 仓库URL
	 */
	private String reposUrl;

	/**
	 *
	 */
	private String eventsUrl;

	/**
	 * 接收事件
	 */
	private String receivedEventsUrl;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 博客地址
	 */
	private String blog;

	/**
	 * 微博地址
	 */
	private String weibo;

	/**
	 * 自我介绍
	 */
	private String bio;

	/**
	 * 公共仓库数
	 */
	private Integer publicRepos;

	/**
	 *
	 */
	private Integer publicGists;

	/**
	 * 粉丝数
	 */
	private Integer followers;

	/**
	 * 关注的人
	 */
	private Integer following;

	/**
	 * star数
	 */
	private Integer stared;

	/**
	 * 关注的仓库
	 */
	private Integer watched;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdAt;

	/**
	 * 更新时间
	 */
	private LocalDateTime updatedAt;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 绑定时间
	 */
	private LocalDateTime bindingDate;

	/**
	 * 授权凭证
	 */
	private String accessToken;

	/**
	 * 刷新凭证
	 */
	private String refreshToken;

	/**
	 * 过期时间
	 */
	private LocalDateTime expires;

	/**
	 * 授权范围
	 */
	private String scope;

	/**
	 * 公司
	 */
	private String company;

	/**
	 * 职务
	 */
	private String profession;

	/**
	 * 微信
	 */
	private String wechat;

	/**
	 * QQ
	 */
	private String qq;

	/**
	 * 领英账户
	 */
	private String linkedin;

	/**
	 * 逻辑删除，0 未删除，1 删除，MySQL 默认值 0，不为 NULL，注解@TableLogic。
	 */
	@TableLogic
	private Integer delFlag;

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}
