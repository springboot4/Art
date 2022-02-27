package com.fxz.common.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fxz
 * @version 1.0
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
	 * 封装前端分页表格所需数据
	 * @param pageInfo pageInfo
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getDataTable(IPage<?> pageInfo) {
		Map<String, Object> data = new HashMap<>();
		data.put("records", pageInfo.getRecords());
		data.put("total", pageInfo.getTotal());
		data.put("current", pageInfo.getCurrent());
		return data;
	}

}
