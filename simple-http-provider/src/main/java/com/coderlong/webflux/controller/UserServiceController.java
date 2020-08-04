package com.coderlong.webflux.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.coderlong.webflux.pojo.User;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author sailongren
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserServiceController {
    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    Mono<User> hello(@PathVariable("name") String name) throws InterruptedException {
        log.info("客户端发起了请求");
        Thread.sleep(3000);
        User user = new User();
        user.setName("hi " + name + " ,i am from port:" + port);
        return Mono.just(user);
    }


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    Mono<String> defaultHello() throws InterruptedException {
        log.info("客户端发起了请求");
        User user = new User();
        Thread.sleep(3000);
        user.setName("hi " + " ,i am from port:" + port);
        return Mono.just(user.toString());
    }

}
