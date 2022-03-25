package com.fxz.system.dinger;

import com.fxz.system.dinger.handler.DingTalkMultiHandler;
import com.github.jaemon.dinger.core.annatations.*;
import com.github.jaemon.dinger.core.entity.DingerResponse;
import com.github.jaemon.dinger.core.entity.ImageTextDeo;
import com.github.jaemon.dinger.core.entity.LinkDeo;
import com.github.jaemon.dinger.core.entity.enums.DingerType;
import com.github.jaemon.dinger.multi.annotations.MultiDinger;
import com.github.jaemon.dinger.multi.annotations.MultiHandler;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-19 20:19
 */
public interface UserDinger {

	/**
	 * 图文类型
	 */
	@DingerImageText
	DingerResponse imageText(List<ImageTextDeo> images);

	/**
	 * link类型, 只支持Dingtalk
	 */
	@DingerLink
	DingerResponse link(LinkDeo link);

	/**
	 * `@`群里指定成员
	 */
	@DingerText(value = "---${username}", phones = "17861275882")
	DingerResponse success(String username, String phones);

	@DingerMarkdown(value = "#### 用户登录通知\n - 用户Id： ${userId}\n - 用户名： ${userName}", title = "用户登录反馈")
	DingerResponse failed(long userId, String userName);

	/**
	 * `@`群里全部成员
	 */
	@DingerText(value = "恭喜用户${loginName}登录成功!", atAll = true)
	DingerResponse login(@Parameter("loginName") String userName);

	/**
	 * wetalk不支持markdown格式的`@`功能
	 */
	@DingerMarkdown(value = "#### 注销登录 @13520200906 @13520200908 \n - 用户Id： ${uId}\n - 用户名： ${loginName}",
			title = "用户登录反馈", phones = { "19806082431", "13520200908" })
	DingerResponse logout(@Parameter("uId") long userId, @Parameter("loginName") String userName);

}
