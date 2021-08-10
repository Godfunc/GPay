package com.godfunc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class OrderExpireApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderExpireApplication.class, args);
    }

}
