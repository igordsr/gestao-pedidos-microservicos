package com.pedidos.service.domain.contract.rule.bussines;

import com.pedidos.service.domain.exception.PedidoNotFoundException;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.model.StatusPedido;

import java.util.List;
import java.util.UUID;

public interface IConsultarPedido {
    Pedido consultarPeloIdentificador(UUID identificador) throws PedidoNotFoundException;
    List<Pedido> consultarPeloStatus(StatusPedido statusPedido) throws PedidoNotFoundException;

}
