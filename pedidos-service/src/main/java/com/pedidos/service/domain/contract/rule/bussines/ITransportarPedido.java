package com.pedidos.service.domain.contract.rule.bussines;

import com.pedidos.service.domain.exception.GestaoDePedidosApplicationException;
import com.pedidos.service.domain.model.Pedido;

import java.util.UUID;

public interface ITransportarPedido {
    Pedido enviar(UUID identificador) throws GestaoDePedidosApplicationException;

    Pedido entregar(UUID identificador) throws GestaoDePedidosApplicationException;
}
