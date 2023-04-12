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

package com.art.common.core.util;

import com.alibaba.fastjson.JSONObject;
import com.art.common.core.model.Result;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/25 11:57
 */
@UtilityClass
public class WebUtil {

	/**
	 * 获取HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return Optional.ofNullable(getRequestAttributes()).map(ServletRequestAttributes::getRequest).orElse(null);
	}

	/**
	 * 获取RequestAttributes
	 */
	public ServletRequestAttributes getRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}

	/**
	 * 获取响应
	 * @return {HttpServletResponse}
	 */
	public HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
			.getResponse();
	}

	/**
	 * 设置响应
	 * @param response HttpServletResponse
	 * @param contentType content-type
	 * @param status http状态码
	 * @param value 响应内容
	 * @throws IOException IOException
	 */
	public static void makeResponse(HttpServletResponse response, String contentType, int status, Object value)
			throws IOException {
		response.setContentType(contentType);
		response.setStatus(status);
		response.getOutputStream().write(JSONObject.toJSONString(value).getBytes());
	}

	/**
	 * 设置失败响应
	 * @param response HttpServletResponse
	 * @param result 响应内容
	 * @throws IOException IOException
	 */
	public static void makeFailureResponse(HttpServletResponse response, Result<Void> result) throws IOException {
		makeResponse(response, MediaType.APPLICATION_JSON_VALUE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, result);
	}

	/**
	 * 设置成功响应
	 * @param response HttpServletResponse
	 * @param result 响应内容
	 */
	public static void makeSuccessResponse(HttpServletResponse response, Result<Object> result) throws IOException {
		makeResponse(response, MediaType.APPLICATION_JSON_VALUE, HttpServletResponse.SC_OK, result);
	}

	public static boolean isAjaxRequest(HttpServletRequest request) {
		return (request.getHeader("X-Requested-With") != null
				&& "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
	}

}
