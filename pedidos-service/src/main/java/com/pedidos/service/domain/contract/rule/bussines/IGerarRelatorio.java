package com.pedidos.service.domain.contract.rule.bussines;

import com.pedidos.service.domain.exception.CustomException;
import com.pedidos.service.domain.model.Pedido;

import java.util.List;

public interface IGerarRelatorio {
    List<Pedido> gerarRelatorioPedidosPagos() throws CustomException;
}
