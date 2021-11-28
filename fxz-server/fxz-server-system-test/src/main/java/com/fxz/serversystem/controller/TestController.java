package com.fxz.serversystem.controller;

import com.fxz.serversystem.service.IHelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author fxz
 */
@RestController
@RequiredArgsConstructor
public class TestController {

    private final IHelloService helloService;

    @GetMapping("/hello")
    public String hello(String name){
        return this.helloService.hello(name);
    }

    @GetMapping("test1")
    @PreAuthorize("hasAnyAuthority('user:add')")
    public String test1() {
        return "拥有'user:add'权限";
    }

    @GetMapping("test2")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public String test2() {
        return "拥有'user:update'权限";
    }

    @GetMapping("user")
    public Principal currentUser(Principal principal) {
        return principal;
    }
}