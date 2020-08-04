package com.coderlong.webflux.service;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coderlong.webflux.annotation.ApiServer;
import com.coderlong.webflux.pojo.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author sailongren
 */
@ApiServer(value = "http://localhost:8081", microName = "webflux-server")
public interface UserService {

    String ROOT = "/user";

    @GetMapping(ROOT + "/")
    Flux<User> getAllUser();

    @GetMapping(ROOT + "/{id}")
    Mono<User> getUserById(@PathVariable("id") String id);

    @DeleteMapping(ROOT + "/{id}")
    Mono<Void> deleteUserById(@PathVariable("id") String id);

    /**
     *  创建用户
     * @param user
     * @return
     */
    @PostMapping(ROOT + "/")
    Mono<User> createUser(@RequestBody Mono<User> user);

    @RequestMapping(value = {ROOT + "/hello/{name}"})
    Mono<User> hello(@PathVariable("name") String name);
}
