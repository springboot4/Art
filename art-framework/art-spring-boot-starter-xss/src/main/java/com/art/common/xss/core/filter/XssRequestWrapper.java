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

package com.art.common.xss.core.filter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HTMLFilter;
import org.springframework.http.MediaType;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/8 11:03
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

	public XssRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	private static String filter(String val) {
		if (StrUtil.isEmpty(val)) {
			return val;
		}

		return FILTER.get().filter(val);
	}

	@Override
	public String getParameter(String name) {
		return filter(super.getParameter(name));
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (ArrayUtil.isEmpty(values)) {
			return values;
		}

		return Arrays.stream(values).map(XssRequestWrapper::filter).toArray(String[]::new);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> parameters = super.getParameterMap();
		if (MapUtil.isEmpty(parameters)) {
			return parameters;
		}

		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			String[] values = entry.getValue();
			for (int i = 0; i < values.length; i++) {
				values[i] = filter(values[i]);
			}
		}

		return parameters;
	}

	@Override
	public String getHeader(String name) {
		return filter(super.getHeader(name));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		boolean json = StrUtil.startWithIgnoreCase(this.getContentType(), MediaType.APPLICATION_JSON_VALUE);
		if (!json) {
			return super.getInputStream();
		}

		String content = IoUtil.readUtf8(super.getInputStream());
		content = filter(content);

		ByteArrayInputStream bain = new ByteArrayInputStream(content.getBytes());
		return new ServletInputStream() {
			@Override
			public int read() {
				return bain.read();
			}

			@Override
			public boolean isFinished() {
				return true;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}
		};
	}

	private static final ThreadLocal<HTMLFilter> FILTER = ThreadLocal.withInitial(() -> {
		Map<String, Object> map = MapUtil.newHashMap();

		Map<String, List<String>> vAllowed = new HashMap<>();
		final ArrayList<String> a_atts = new ArrayList<>();
		a_atts.add("href");
		a_atts.add("target");
		vAllowed.put("a", a_atts);

		final ArrayList<String> img_atts = new ArrayList<>();
		img_atts.add("src");
		img_atts.add("width");
		img_atts.add("height");
		img_atts.add("alt");
		vAllowed.put("img", img_atts);

		final ArrayList<String> no_atts = new ArrayList<>();
		vAllowed.put("b", no_atts);
		vAllowed.put("strong", no_atts);
		vAllowed.put("i", no_atts);
		vAllowed.put("em", no_atts);

		String[] vSelfClosingTags = new String[] { "img" };
		String[] vNeedClosingTags = new String[] { "a", "b", "strong", "i", "em" };
		String[] vDisallowed = new String[] {};
		String[] vAllowedProtocols = new String[] { "http", "mailto", "https" };
		String[] vProtocolAtts = new String[] { "src", "href" };
		String[] vRemoveBlanks = new String[] { "a", "b", "strong", "i", "em" };
		String[] vAllowedEntities = new String[] { "amp", "gt", "lt", "quot" };

		boolean stripComment = true;
		boolean encodeQuotes = false;
		boolean alwaysMakeTags = true;

		map.put("vAllowed", vAllowed);
		map.put("vSelfClosingTags", vSelfClosingTags);
		map.put("vNeedClosingTags", vNeedClosingTags);
		map.put("vDisallowed", vDisallowed);
		map.put("vAllowedProtocols", vAllowedProtocols);
		map.put("vProtocolAtts", vProtocolAtts);
		map.put("vRemoveBlanks", vRemoveBlanks);
		map.put("vAllowedEntities", vAllowedEntities);
		map.put("stripComment", stripComment);
		map.put("encodeQuotes", encodeQuotes);
		map.put("alwaysMakeTags", alwaysMakeTags);

		return new HTMLFilter(map);
	});

}
