package com.pedidos.service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "produto", url = "http://localhost:8082")
public interface ProdutoServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/produto/filterById={id}", produces = "application/json")
    List<Produto> getProdutoById(@PathVariable("id") List<UUID> id);

}
