package com.pedidos.service.infrastructure.feign;

import com.pedidos.service.infrastructure.feign.vo.ProdutoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "produto", url = "${produto.service.url}")
public interface ProdutoServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/produto/filterById={id}", produces = "application/json")
    List<ProdutoVO> getProdutoById(@PathVariable("id") List<UUID> id);

    @RequestMapping(method = RequestMethod.PUT, value = "/produto/{id}/decrementarEstoque/{quantidade}", produces = "application/json")
    ProdutoVO decrementarEstoque(@PathVariable("id") UUID id, @PathVariable("quantidade") int quantidade);

}
