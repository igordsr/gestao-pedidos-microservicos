package com.catalogoprodutosservice.configuration;

import com.catalogoprodutosservice.model.Produto;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class ProdutoProcessor implements ItemProcessor<Produto, Produto> {

    @Override
    public Produto process(final Produto item) throws Exception {
        item.setId(UUID.randomUUID());
        item.setStatus(Boolean.TRUE);
        return item;
    }
}
