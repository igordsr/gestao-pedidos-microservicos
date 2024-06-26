package com.pedidos.service.domain.contract.rule.bussines;

import com.pedidos.service.domain.exception.CustomException;
import com.pedidos.service.domain.model.Pedido;

import java.util.UUID;

public interface ILiquidarPedido {
    Pedido liquidarPedido(final UUID identificador) throws CustomException;
}
