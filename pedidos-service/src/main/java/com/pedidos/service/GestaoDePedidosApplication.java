package com.pedidos.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class GestaoDePedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestaoDePedidosApplication.class, args);
    }

}
