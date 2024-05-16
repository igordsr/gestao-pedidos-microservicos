package com.pedidos.service.infrastructure.service;

import com.pedidos.service.domain.contract.IProdutoContract;
import com.pedidos.service.domain.model.Item;
import com.pedidos.service.infrastructure.feign.Produto;
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
        final List<Produto> produtos = this.produtoServiceClient.getProdutoById(itens);
        final List<Item> itensFound = new ArrayList<>();
        for (Produto produto : produtos) {
            Item item = new Item(produto.id(), produto.qtdEstoque());
            itensFound.add(item);
        }
        return itensFound;
    }
}
