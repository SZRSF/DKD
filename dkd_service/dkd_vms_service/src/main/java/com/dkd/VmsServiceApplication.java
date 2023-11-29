package com.dkd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@EnableCaching
@EnableConfigurationProperties
@EnableFeignClients
@EnableTransactionManagement
@SpringBootApplication
public class VmsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run( VmsServiceApplication.class, args);
    }
}
