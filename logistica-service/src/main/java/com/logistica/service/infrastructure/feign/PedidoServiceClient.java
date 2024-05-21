package com.logistica.service.infrastructure.feign;

import com.logistica.service.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "pedido", url = "${pedido.service.url}", configuration = FeignConfig.class)
public interface PedidoServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/pedido/relatorio-pedidos-pagos", produces = "application/json")
    List<Pedido> getRelatorioPedidosPagos();


    @RequestMapping(method = RequestMethod.PUT, value = "/pedido/{id}/transportar", produces = "application/json")
    Pedido confirmarTransporte(@PathVariable("id") UUID id);

    @RequestMapping(method = RequestMethod.PUT, value = "/pedido/{id}/entregar", produces = "application/json")
    Pedido confirmarEntrega(@PathVariable("id") UUID id);


}
