package com.coderlong.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.coderlong.webflux.annotation.ApiServerScan;

/**
 * @author sailongren
 */
@SpringBootApplication(scanBasePackages = "com.coderlong.webflux")
@ApiServerScan(packages = "com.coderlong.webflux.service")
public class SpringBootWebFluxClient {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebFluxClient.class, args);
    }
}
