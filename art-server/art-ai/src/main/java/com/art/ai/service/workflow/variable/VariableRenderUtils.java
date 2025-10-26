package com.art.ai.service.workflow.variable;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.art.json.sdk.util.JacksonUtil;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * 渲染变量（仅替换占位符内部的点为下划线，并去掉占位符花括号）
	 *
	 * 例1: 入参: "${output.node_6.output}.userEmotion" 出参:
	 * "output_node_6_output.userEmotion"
	 *
	 * 例2: 入参: "http://a.b.com/${env.region}/x" 出参: "http://a.b.com/env_region/x"
	 */
	public String render(String text) {
		if (StringUtils.isBlank(text)) {
			return "";
		}
		Pattern p = Pattern.compile("\\$\\{([^}]+)}");
		Matcher m = p.matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String inner = m.group(1).replace('.', '_');
			m.appendReplacement(sb, Matcher.quoteReplacement(inner));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 格式化（仅规范化占位符内部的点为下划线，再交给模板引擎渲染）
	 *
	 * 例1: 入参: text = "Hello, ${user.name}", args = {"user_name":"Alice"} 出参: "Hello,
	 * Alice"
	 *
	 * 例2: 入参: text = "${output.node_6.output}.userEmotion", args =
	 * {"output_node_6_output":"happy"} 出参: "happy.userEmotion"
	 */
	public String format(String text, Map<String, Object> args) {
		if (StringUtils.isBlank(text)) {
			return "";
		}

		String normalized = normalizeTemplateVariables(text);
		Template template = ENGINE.getTemplate(normalized);

		HashMap<String, String> inputs = new HashMap<>();
		args.forEach((k, v) -> {
			inputs.put(k, JacksonUtil.toJsonString(v));
		});

		return template.render(inputs);
	}

	/**
	 * 仅对模板变量占位符（如 ${a.b.c}）内部做点转下划线，生成 ${a_b_c}
	 */
	private String normalizeTemplateVariables(String text) {
		Pattern p = Pattern.compile("\\$\\{([^}]+)}");
		Matcher m = p.matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String inner = m.group(1).replace('.', '_');
			String replaced = "${" + inner + "}";
			m.appendReplacement(sb, Matcher.quoteReplacement(replaced));
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
