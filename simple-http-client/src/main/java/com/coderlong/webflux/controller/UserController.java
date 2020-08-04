package com.coderlong.webflux.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.coderlong.webflux.pojo.User;
import com.coderlong.webflux.service.UserService;

import reactor.core.publisher.Mono;

/**
 * @author sailongren
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Mono<User> getUserWithName() {
        return userService.hello("sss");
    }
}
