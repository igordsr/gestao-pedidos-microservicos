package com.pedidos.service.domain.contract;

import com.pedidos.service.domain.contract.rule.bussines.IAtualizarEstoqueProduto;
import com.pedidos.service.domain.contract.rule.bussines.IConsultarProduto;

public interface IProdutoContract extends IConsultarProduto, IAtualizarEstoqueProduto {
}
