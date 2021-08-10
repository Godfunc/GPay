package com.godfunc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RollChannelRiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(RollChannelRiskApplication.class, args);
    }

}
