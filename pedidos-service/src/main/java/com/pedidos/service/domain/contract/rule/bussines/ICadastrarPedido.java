package com.pedidos.service.domain.contract.rule.bussines;

import com.pedidos.service.domain.model.Pedido;

public interface ICadastrarPedido {
    Pedido cadastrar(final Pedido pedido);
}
