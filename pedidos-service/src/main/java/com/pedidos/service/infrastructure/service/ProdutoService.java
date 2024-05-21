package com.pedidos.service.infrastructure.service;

import com.pedidos.service.domain.contract.IProdutoContract;
import com.pedidos.service.domain.exception.EntidadeNaoProcessavelException;
import com.pedidos.service.domain.model.Item;
import com.pedidos.service.infrastructure.feign.vo.ProdutoVO;
import com.pedidos.service.infrastructure.feign.ProdutoServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService implements IProdutoContract {
    private final ProdutoServiceClient produtoServiceClient;

    @Autowired
    public ProdutoService(ProdutoServiceClient produtoServiceClient) {
        this.produtoServiceClient = produtoServiceClient;
    }

    @Override
    public List<Item> consultarProdutos(List<UUID> itens) {
        final List<ProdutoVO> produtoVOS = this.produtoServiceClient.getProdutoById(itens);
        final List<Item> itensFound = new ArrayList<>();
        for (ProdutoVO produtoVO : produtoVOS) {
            Item item = new Item(produtoVO.id(), produtoVO.qtdEstoque());
            itensFound.add(item);
        }
        return itensFound;
    }

    @Override
    public Item diminuirQuantidadeProdutoEstoque(UUID id, Integer quantidade) throws EntidadeNaoProcessavelException {
        final ProdutoVO produtoVO = this.produtoServiceClient.decrementarEstoque(id, quantidade);
        return new Item(produtoVO.id(), produtoVO.qtdEstoque());
    }
}
