package com.pedidos.service.domain.contract.rule.bussines;

import com.pedidos.service.domain.exception.CustomException;
import com.pedidos.service.domain.model.Pedido;

import java.util.UUID;

public interface ITransportarPedido {
    Pedido enviar(UUID identificador) throws CustomException;

    Pedido entregar(UUID identificador) throws CustomException;
}
