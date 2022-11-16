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
import com.art.common.mp.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-27 21:50
 */
@Slf4j
public class FxzUtil {

	/**
	 * 正则校验
	 * @param regex 正则表达式字符串
	 * @param value 要匹配的字符串
	 * @return 正则校验结果
	 */
	public static boolean match(String regex, String value) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
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
