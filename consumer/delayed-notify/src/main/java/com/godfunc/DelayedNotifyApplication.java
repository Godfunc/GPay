package com.godfunc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DelayedNotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelayedNotifyApplication.class, args);
    }

}
