package com.rakers.eauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(EAuthApplication.class, args);
    }

}
