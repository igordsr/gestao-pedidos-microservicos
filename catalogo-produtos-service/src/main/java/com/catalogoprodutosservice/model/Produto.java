package com.catalogoprodutosservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @NotNull
    @PositiveOrZero
    private Double preco;

    @PositiveOrZero
    @Column(name = "qtd_estoque", nullable = false)
        private Integer qtdEstoque;

    @Column(nullable = false)
    private boolean status = Boolean.TRUE;
}
