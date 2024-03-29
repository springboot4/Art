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

package com.art.common.log.core.aspect;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import com.art.common.log.core.annotation.OperLogAnn;
import com.art.common.log.core.enums.BusinessStatus;
import com.art.common.log.core.service.AsyncLogService;
import com.art.core.common.exception.ArtException;
import com.art.core.common.model.Result;
import com.art.core.common.util.WebUtil;
import com.art.system.api.log.dto.OperLogDTO;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 操作日志记录处理切面
 *
 * @author fxz
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class LogAspect {

	private final AsyncLogService asyncLogService;

	/**
	 * 处理OperLog注解，使用环绕通知
	 */
	@SuppressWarnings("unchecked")
	@Around("@annotation(operLogAnn)")
	public Result<Object> around(ProceedingJoinPoint point, OperLogAnn operLogAnn) {
		OperLogDTO operLog = new OperLogDTO();
		buildLog(point, operLogAnn, operLog);

		Long startTime = System.currentTimeMillis();
		Result<Object> result = null;
		try {
			result = (Result<Object>) point.proceed();
		}
		catch (Throwable e) {
			handleException(operLog, e.getLocalizedMessage());
			// 切面要将捕获到的异常抛出
			throw new ArtException(e.getLocalizedMessage());
		}
		finally {
			Long endTime = System.currentTimeMillis();
			// 方法执行时间
			long time = endTime - startTime;

			// 赋值
			operLog.setTime(time);

			log.info("异步保存:{}", Thread.currentThread().getId());
			// 异步保存
			asyncLogService.saveSysLog(operLog);
		}
		return result;
	}

	private void buildLog(JoinPoint point, OperLogAnn operLogAnn, OperLogDTO operLog) {
		HttpServletRequest request = WebUtil.getRequest();

		// 模块名
		String title = operLogAnn.title();

		// 获取ip地址
		String ip = ServletUtil.getClientIP(request);

		// 请求路径
		String path = URLUtil.getPath(request.getRequestURI());

		// 操作用户
		AtomicReference<String> userName = new AtomicReference<>();
		Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).ifPresent(authentication -> {
			userName.set(authentication.getName());
		});

		// 方法名称
		String method = point.getTarget().getClass().getName() + StringPool.DOT + point.getSignature().getName()
				+ StringPool.LEFT_BRACKET + StringPool.RIGHT_BRACKET;

		// 请求方式
		String requestMethod = request.getMethod();

		// 请求参数
		String param = null;
		if (operLogAnn.isSaveRequestData()) {
			MethodSignature signature = (MethodSignature) point.getSignature();

			// 获取方法参数名称
			String[] parameterNames = signature.getParameterNames();

			// 获取方法实际参数值
			Object[] args = point.getArgs();

			JSONObject json = new JSONObject();
			for (int i = 0; i < parameterNames.length; i++) {
				json.set(parameterNames[i], args[i]);
			}

			param = json.toString();
		}

		// 业务类型
		Integer businessType = operLogAnn.businessType().getValue();

		log.info("方法:{}", method);

		// 赋值
		operLog.setTitle(title)
			.setOperIp(ip)
			.setOperUrl(path)
			.setOperName(userName.get())
			.setMethod(method)
			.setRequestMethod(requestMethod)
			.setOperParam(param)
			.setBusinessType(businessType)
			.setStatus(BusinessStatus.SUCCESS.getValue());
	}

	private void handleException(OperLogDTO operLog, String msg) {
		operLog.setStatus(BusinessStatus.FAIL.getValue());
		operLog.setErrorMsg(StringUtils.substring(msg, 0, 2000));
	}

}
