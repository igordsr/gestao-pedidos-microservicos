package com.pedidos.service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(value = "cliente", url = "http://localhost:8081")
public interface ClienteServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/cliente/{id}", produces = "application/json")
    Cliente getClienteById(@PathVariable("id") UUID id);

}
