package com.coderlong.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.coderlong.webflux.pojo.User;
import com.coderlong.webflux.service.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author sailongren
 */
@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;
//
//    @Autowired
//    private UserMapper userMapper;

    @RequestMapping(value = "/helloWorld", method = RequestMethod.GET)
    public Mono<User> getUserWithNameSync() {
//        userMapper.query(1);
        return userService.hello("hello world");
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Mono<User> getUserWithName() {
        User fallback = new User("fallback");
        long start = System.currentTimeMillis();
        Mono<User> mono1 = userService.hello("Java").onErrorReturn(fallback).doOnSuccess(r -> {
            log.info("成功执行1, 线程: {}", Thread.currentThread().getName());
        });
        Mono<User> mono2 = userService.hello("C++").onErrorReturn(fallback).doOnSuccess(r -> {
            log.info("成功执行2, 线程: {}", Thread.currentThread().getName());
        });
        Mono<User> mono3 = userService.hello("golang").onErrorReturn(fallback).doOnSuccess(r -> {
            log.info("成功执行3, 线程: {}", Thread.currentThread().getName());
        });
        User user = new User();
        Mono res = Mono.zip(mono1, mono2, mono3)
                .doOnSuccess(r -> {
                    log.info("成功执行, 线程: {}", Thread.currentThread().getName());
                })
                .doOnError(err -> {
                    log.error("错误类型 ", err);
                })
                .flatMap(tup -> {
                    log.info("耗时 : {}", (System.currentTimeMillis() - start) / 1000);
                    log.info("结果 : {}", tup);
                    user.setName(tup.toString());
                    return Mono.just(user);
                });
        return res;
    }
}
