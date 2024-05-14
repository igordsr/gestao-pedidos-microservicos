package com.catalogoprodutosservice.controller.exception.model;

public final class ProdutoAlreadyExistsException extends CatalogoProdutosServiceApplicationException {
    public ProdutoAlreadyExistsException(final String nome) {
        super(String.format("Produto %s já está cadastrado no sistema.", nome.toUpperCase()));
    }
}
