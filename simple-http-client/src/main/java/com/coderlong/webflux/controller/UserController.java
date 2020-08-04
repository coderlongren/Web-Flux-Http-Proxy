package com.coderlong.webflux.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.coderlong.webflux.pojo.User;
import com.coderlong.webflux.service.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author sailongren
 */
@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/helloWorld", method = RequestMethod.GET)
    public Mono<User> getUserWithNameSync() {
        return userService.hello("hello world");
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Mono<User> getUserWithName() {
        long start = System.currentTimeMillis();
        Mono<User> mono1 = userService.hello("Java");
        Mono<User> mono2 = userService.hello("C++");
        Mono<User> mono3 = userService.hello("golang");
        User user = new User();
        Mono res = Mono.zip(mono1, mono2, mono3)
                .flatMap(tup -> {
                    log.info("耗时 : {}", (System.currentTimeMillis() - start) / 1000);
                    log.info("结果 : {}", tup);
                    user.setName(tup.toString());
                    return Mono.just(user);
                });
        return res;
    }
}
