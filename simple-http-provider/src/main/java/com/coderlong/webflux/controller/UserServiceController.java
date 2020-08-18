package com.coderlong.webflux.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.coderlong.webflux.pojo.User;
import com.coderlong.webflux.service.AsyncTask1;
import com.coderlong.webflux.service.ISyncTask;

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

    @Resource(name = "asyncTask1")
    private ISyncTask asyncTask1;

    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    Mono<User> hello(@PathVariable("name") String name) throws InterruptedException {
        log.info("客户端发起了请求");
        Thread.sleep(3000);
        User user = new User();
        user.setName("hi " + name + " ,i am from port:" + port);

        System.out.println();

        return Mono.just(user);
    }

    public static void main(String[] args) {
        System.out.println(TimeUnit.MINUTES.toMillis(3));
        ISyncTask task = new AsyncTask1();
        List<String> strList = new ArrayList<>();
        List<?> list1 = strList;
        try {
            System.out.println(Stream.of(task.execute("")).collect(Collectors.toList()));
            List<CompletableFuture<Object>> list = Stream.of(task.execute("")).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
        }
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
