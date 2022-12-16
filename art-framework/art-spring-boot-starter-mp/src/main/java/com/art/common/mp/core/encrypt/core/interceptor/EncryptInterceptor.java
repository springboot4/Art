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

package com.art.common.mp.core.encrypt.core.interceptor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.art.common.mp.core.encrypt.core.annotation.EncryptionData;
import com.art.common.mp.core.encrypt.core.propertie.EncryptProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * 数据加密拦截器
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/15 14:01
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class EncryptInterceptor implements Interceptor {

    private final EncryptProperties encryptProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取方法参数
        Object[] args = invocation.getArgs();
        Class<?>[] params = {MappedStatement.class, Object.class};

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
     *
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
