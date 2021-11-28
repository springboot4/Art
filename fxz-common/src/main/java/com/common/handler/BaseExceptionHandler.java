package com.common.handler;

import com.common.entity.FxzResponse;
import com.common.exception.FxzAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常处理器
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 11:42
 */
@Slf4j
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public FxzResponse handleException(Exception e) {
        log.error("系统内部异常，异常信息", e);
        return new FxzResponse().message("系统内部异常");
    }

    @ExceptionHandler(value = FxzAuthException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public FxzResponse handleFxzAuthException(FxzAuthException e) {
        log.error("系统错误", e);
        return new FxzResponse().message(e.getMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public FxzResponse handleAccessDeniedException() {
        return new FxzResponse().message("没有权限访问该资源");
    }
}
