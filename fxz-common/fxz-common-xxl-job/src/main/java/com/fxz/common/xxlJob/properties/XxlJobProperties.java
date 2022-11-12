/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.fxz.common.xxlJob.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/8/13 20:19
 */
@Data
@ConfigurationProperties(prefix = "fxz.common.xxl-job")
public class XxlJobProperties {

	/**
	 * 调度中心部署根地址：如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
	 */
	private String adminAddresses;

	/**
	 * 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
	 */
	private String appname = "xxl-job-executor";

	/**
	 * 服务注册地址,优先使用该配置作为注册地址 为空时使用内嵌服务 ”IP:PORT“ 作为注册地址 从而更灵活的支持容器类型执行器动态IP和动态映射端口问题
	 */
	private String address;

	/**
	 * 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP ，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和
	 * "调度中心请求并触发任务"
	 */
	private String ip;

	/**
	 * 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
	 */
	private Integer port = 0;

	/**
	 * 执行器通讯TOKEN [选填]：非空时启用；
	 */
	private String accessToken;

	/**
	 * 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
	 */
	private String logPath = "logs/applogs/xxl-job/jobhandler";

	/**
	 * 执行器日志保存天数 [选填] ：值大于3时生效，启用执行器Log文件定期清理功能，否则不生效；
	 */
	private Integer logRetentionDays = 30;

}
