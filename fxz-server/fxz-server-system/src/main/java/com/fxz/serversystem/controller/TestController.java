package com.fxz.serversystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author fxz
 */
@RestController
public class TestController {

    @GetMapping("/info")
    public String test() {
        return "fxz-server-system";
    }

    @GetMapping("/user")
    public Principal currentUser(Principal principal) {
        return principal;
    }

    @GetMapping("/hello")
    public String hello(String name) {
        return "hello" + name;
    }

}