package com.logistica.service.infrastructure.feign;

import com.logistica.service.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(value = "cliente", url = "${cliente.service.url}", configuration = FeignConfig.class)
public interface ClienteServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/cliente/{id}", produces = "application/json")
    src.main.java.com.logistica.service.infrastructure.feign.Cliente getClienteById(@PathVariable("id") UUID id);
}
