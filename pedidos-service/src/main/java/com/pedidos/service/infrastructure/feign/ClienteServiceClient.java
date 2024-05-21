package com.pedidos.service.infrastructure.feign;

import com.pedidos.service.infrastructure.config.FeignConfig;
import com.pedidos.service.infrastructure.feign.vo.ClienteVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(value = "cliente", url = "${cliente.service.url}", configuration = FeignConfig.class)
public interface ClienteServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/cliente/{id}", produces = "application/json")
    ClienteVO getClienteById(@PathVariable("id") UUID id);

}
