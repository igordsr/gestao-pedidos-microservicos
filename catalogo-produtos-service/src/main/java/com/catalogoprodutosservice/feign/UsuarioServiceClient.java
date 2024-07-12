package com.catalogoprodutosservice.feign;


import com.catalogoprodutosservice.configuration.FeignConfig;
import com.catalogoprodutosservice.feign.vo.UserDetailsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "usuario-service", url = "${usuario.service.url}", configuration = FeignConfig.class)
public interface UsuarioServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/auth", produces = "application/json")
    UserDetailsVO validateToken(@RequestHeader("Authorization") String token);

}
