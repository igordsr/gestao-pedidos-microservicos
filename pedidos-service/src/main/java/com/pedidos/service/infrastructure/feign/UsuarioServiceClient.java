package com.pedidos.service.infrastructure.feign;

import com.pedidos.service.infrastructure.config.FeignConfig;
import com.pedidos.service.infrastructure.feign.vo.UserDetailsVO;
import com.pedidos.service.infrastructure.feign.vo.UsuarioVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(value = "usuario", url = "${usuario.service.url}", configuration = FeignConfig.class)
public interface UsuarioServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/usuario/{id}", produces = "application/json")
    UsuarioVO getUsuarioById(@PathVariable("id") UUID id);

    @RequestMapping(method = RequestMethod.GET, value = "/auth", produces = "application/json")
    UserDetailsVO validateToken(@RequestHeader("Authorization") String token);

}
