package com.rakers.ecsv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ECsvApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECsvApplication.class, args);
    }

}
