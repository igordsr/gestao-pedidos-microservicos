package src.main.java.com.logistica.service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import src.main.java.com.logistica.service.infrastructure.config.FeignConfig;

import java.util.List;

@FeignClient(value = "pedido", url = "${produto.service.url}", configuration = FeignConfig.class)
public interface PedidoServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/pedido/relatorio-pedidos-pagos", produces = "application/json")
    List<Pedido> getRelatorioPedidosPagos();
}
