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

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.art.common.mp.core.encrypt.core.annotation.EncryptionData;
import com.art.common.mp.core.encrypt.core.propertie.EncryptProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据解密拦截器
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/15 14:00
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}))
public class DecryptInterceptor implements Interceptor {

    private final EncryptProperties encryptProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        // 未开启字段加解密
        if (!encryptProperties.isEnableFieldDecrypt()) {
            return result;
        }

        return this.aesDecrypt(result);
    }

    private Object aesDecrypt(Object obj) {
        // 查询结果为集合
        if (obj instanceof List<?>) {
            return ((ArrayList<?>) obj).stream().peek(o -> {
                if (Objects.nonNull(o)) {
                    // 过滤出需要加解密的字段
                    Field[] fields = ReflectUtil.getFields(o.getClass(),
                            field -> Objects.nonNull(field.getAnnotation(EncryptionData.class)));
                    // 通过反射对字段进行aes解密
                    Arrays.stream(fields).forEach(f -> ReflectUtil.setFieldValue(o, f,
                            this.aesDecryptValue(ReflectUtil.getFieldValue(o, f))));
                }
            }).collect(Collectors.toList());
        }

        // 查询结果为对象 过滤出需要加解密的字段
        Field[] fields = ReflectUtil.getFields(obj.getClass(),
                field -> Objects.nonNull(field.getAnnotation(EncryptionData.class)));
        // 通过反射对字段进行aes解密
        Arrays.stream(fields).forEach(
                f -> ReflectUtil.setFieldValue(obj, f, this.aesDecryptValue(ReflectUtil.getFieldValue(obj, f))));

        return obj;
    }

    /**
     * aes解密
     */
    public Object aesDecryptValue(Object fieldValue) {
        // 只对字符串类型进行处理
        if (fieldValue instanceof String) {
            AES aes = SecureUtil.aes(encryptProperties.getFieldDecryptKey().getBytes(StandardCharsets.UTF_8));
            return new String(aes.decrypt(Base64.decode((String) fieldValue)), StandardCharsets.UTF_8);
        }

        return fieldValue;
    }

}
