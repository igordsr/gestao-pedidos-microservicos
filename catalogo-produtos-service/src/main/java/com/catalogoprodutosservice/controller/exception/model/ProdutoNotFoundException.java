package com.catalogoprodutosservice.controller.exception.model;

public final class ProdutoNotFoundException extends CatalogoProdutosServiceApplicationException {
    public ProdutoNotFoundException() {
        super("Produto não foi encontrado");
    }
}