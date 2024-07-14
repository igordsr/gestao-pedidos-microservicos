package com.pedidos.service.domain.contract.rule.bussines;

import com.pedidos.service.domain.exception.RegistroNaoEncontradoException;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.model.StatusPedido;

import java.util.List;
import java.util.UUID;

public interface IConsultarPedido {
    Pedido consultarPeloIdentificador(UUID identificador) throws RegistroNaoEncontradoException;

    List<Pedido> consultarPeloStatus(StatusPedido statusPedido) throws RegistroNaoEncontradoException;

    List<Pedido> consultarPeloStatus(List<StatusPedido> statusPedido) throws RegistroNaoEncontradoException;

    List<Pedido> consultarPeloIdClienteByToken() throws RegistroNaoEncontradoException;

}
