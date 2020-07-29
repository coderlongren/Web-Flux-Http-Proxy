package com.coderlong.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author sailongren
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringWebFluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringWebFluxApplication.class, args);
    }
}
