package com.art.ai.service.workflow.variable;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.art.json.sdk.util.JacksonUtil;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/16 00:22
 */
@UtilityClass
public class VariableRenderUtils {

	public static final TemplateEngine ENGINE = TemplateUtil.createEngine(new TemplateConfig());

	/**
	 * 渲染变量
	 */
	public String render(String text) {
		if (StringUtils.isBlank(text)) {
			return "";
		}
		return text.replaceAll("\\$\\{(.+?)}", "$1").replace(".", "_");
	}

	/**
	 * 格式化
	 */
	public String format(String text, Map<String, Object> args) {
		if (StringUtils.isBlank(text)) {
			return "";
		}

		text = text.replace(".", "_");

		Template template = ENGINE.getTemplate(text);

		HashMap<String, String> inputs = new HashMap<>();
		args.forEach((k, v) -> {
			inputs.put(k, JacksonUtil.toJsonString(v));
		});

		return template.render(inputs);
	}

}
