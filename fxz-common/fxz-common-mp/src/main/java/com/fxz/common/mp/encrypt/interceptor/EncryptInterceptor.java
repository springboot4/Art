package com.fxz.common.mp.encrypt.interceptor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.fxz.common.mp.encrypt.annotation.EncryptionData;
import com.fxz.common.mp.encrypt.propertie.EncryptProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * 数据加密拦截器
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/9/15 14:01
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Intercepts(@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }))
public class EncryptInterceptor implements Interceptor {

	private final EncryptProperties encryptProperties;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 获取方法参数
		Object[] args = invocation.getArgs();
		Class<?>[] params = { MappedStatement.class, Object.class };

		// 不符合加密条件 直接放行
		if (!encryptProperties.isEnableFieldDecrypt() || args.length != params.length) {
			return invocation.proceed();
		}

		// 获取要加密的参数
		Object obj = args[1];
		// aes加密参数
		this.aesEncrypt(obj);

		// 放行方法
		return invocation.proceed();
	}

	private void aesEncrypt(Object obj) {
		if (Objects.isNull(obj)) {
			return;
		}

		// 过滤出对象上有加密注解的字段
		Field[] fields = ReflectUtil.getFields(obj.getClass(),
				field -> Objects.nonNull(field.getAnnotation(EncryptionData.class)));

		// 通过反射为字段设置加密后的值
		Arrays.stream(fields).forEach(
				f -> ReflectUtil.setFieldValue(obj, f, this.aesEncryptValue(ReflectUtil.getFieldValue(obj, f))));
	}

	/**
	 * aes加密方法
	 * @param fieldValue 属性值
	 * @return 加密值
	 */
	public Object aesEncryptValue(Object fieldValue) {
		// 只对字符串类型进行加密
		if (fieldValue instanceof String) {
			AES aes = SecureUtil.aes(encryptProperties.getFieldDecryptKey().getBytes(StandardCharsets.UTF_8));
			return aes.encryptBase64((String) fieldValue);
		}

		return fieldValue;
	}

}
