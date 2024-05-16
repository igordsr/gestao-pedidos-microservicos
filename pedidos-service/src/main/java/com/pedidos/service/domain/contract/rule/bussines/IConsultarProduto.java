package com.pedidos.service.domain.contract.rule.bussines;

import com.pedidos.service.domain.model.Item;

import java.util.List;
import java.util.UUID;

public interface IConsultarProduto {
    List<Item> consultarProdutos(List<UUID> itens);
}
