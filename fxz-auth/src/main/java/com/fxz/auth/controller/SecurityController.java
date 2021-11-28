package com.fxz.auth.controller;

import com.common.entity.FxzResponse;
import com.common.exception.FxzAuthException;
import com.common.exception.ValidateCodeException;
import com.fxz.auth.service.ValidateCodeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 16:28
 */
@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final ConsumerTokenServices consumerTokenServices;

    private final ValidateCodeService validateCodeService;

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }

    @GetMapping("/oauth/test")
    public String testOauth() {
        return "oauth";
    }

    @GetMapping("/user")
    public Principal currentUser(Principal principal) {
        return principal;
    }

    @DeleteMapping("/signout")
    public FxzResponse signout(HttpServletRequest request) throws FxzAuthException {
        String authorization = request.getHeader("Authorization");
        String token = StringUtils.replace(authorization, "bearer ", "");
        FxzResponse fxzResponse = new FxzResponse();
        if (!consumerTokenServices.revokeToken(token)) {
            throw new FxzAuthException("退出登录失败");
        }
        return fxzResponse.message("退出登录成功");
    }
}
