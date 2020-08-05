package com.coderlong.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.coderlong.webflux.annotation.ApiServerScan;

/**
 * @author sailongren
 */
@SpringBootApplication()
@ApiServerScan(packages = "com.coderlong.webflux.service")
@ComponentScan({"com.coderlong.webflux"})
public class SpringBootWebFluxClient {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebFluxClient.class, args);
    }
}
