package com.catalogoprodutosservice.controller.exception.model;

public final class ProdutoInsuficienteException extends CatalogoProdutosServiceApplicationException {
    public ProdutoInsuficienteException(final String nome) {
        super(String.format("Não há unidades suficientes do produto %S para a demanda solicitada", nome.toUpperCase()));
    }
}
