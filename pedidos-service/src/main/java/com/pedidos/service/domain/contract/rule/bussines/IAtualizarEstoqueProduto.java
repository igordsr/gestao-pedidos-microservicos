package com.pedidos.service.domain.contract.rule.bussines;

import com.pedidos.service.domain.exception.EntidadeNaoProcessavelException;
import com.pedidos.service.domain.model.Item;

import java.util.UUID;

public interface IAtualizarEstoqueProduto {
    Item diminuirQuantidadeProdutoEstoque(UUID produto, Integer quantidade) throws EntidadeNaoProcessavelException;

}
