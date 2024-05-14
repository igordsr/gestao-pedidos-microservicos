package com.catalogoprodutosservice.dto;

import com.catalogoprodutosservice.model.Produto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.util.Assert;

import java.util.UUID;

public record ProdutoDTO(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID id,
        @Schema(example = "Camiseta")
        @NotNull(message = "Nome do produto não pode estar em nulo.")
        @NotBlank(message = "Nome do produto não pode estar em Branco.")
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        @Schema(example = "Camiseta de algodão com estampa")
        @NotNull(message = "Descrição do produto não pode estar em nulo.")
        @NotBlank(message = "Descrição do produto não pode estar em Branco.")
        @NotBlank(message = "O descrição não pode estar em branco")
        String descricao,

        @Schema(example = "29.99")
        @NotNull(message = "Preço do produto não pode estar em nulo.")
        @PositiveOrZero(message = "Preço do produto não pode ser menor que zero")
        Double preco,
        @Schema(example = "3")
        @NotNull(message = "Quantidade do produto não pode estar em nulo.")
        @PositiveOrZero(message = "Quantidade do produto não pode ser menor que zero")
        Integer qtdEstoque
) {
    public Produto toProduto() {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setQtdEstoque(qtdEstoque);
        return produto;
    }

    public static ProdutoDTO getInstance(final Produto produto) {
        Assert.notNull(produto, "Objeto não pode ser nulo");
        return new ProdutoDTO(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.getQtdEstoque());
    }
}
