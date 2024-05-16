package com.pedidos.service.domain.contract;

import com.pedidos.service.domain.contract.rule.bussines.ICadastrarPedido;
import com.pedidos.service.domain.contract.rule.bussines.IGerarRelatorio;
import com.pedidos.service.domain.contract.rule.bussines.ILiquidarPedido;
import com.pedidos.service.domain.contract.rule.bussines.ITransportarPedido;
import com.pedidos.service.domain.exception.PedidoNotFoundException;
import com.pedidos.service.domain.model.Pedido;

import java.util.UUID;

public interface IPedidoContract extends ICadastrarPedido, ILiquidarPedido, IGerarRelatorio, ITransportarPedido {
    Pedido consultarPeloIdentificador(UUID identificador) throws PedidoNotFoundException;
}
