package com.logistica.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class LogisticaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticaServiceApplication.class, args);
    }

}
