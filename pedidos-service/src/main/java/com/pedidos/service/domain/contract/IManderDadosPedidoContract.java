package com.pedidos.service.domain.contract;

import com.pedidos.service.domain.contract.rule.bussines.IConsultarPedido;
import com.pedidos.service.domain.contract.rule.persistence.IManterDados;
import com.pedidos.service.domain.model.Pedido;

public interface IManderDadosPedidoContract extends IManterDados<Pedido>, IConsultarPedido {

}
