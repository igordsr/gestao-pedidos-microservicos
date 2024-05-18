package com.pedidos.service.domain.model;

import lombok.Getter;

@Getter
public enum StatusPedido {
    AGUARDANDO_PAGAMENTO(1, "AGUARDANDO PAGAMENTO"),
    PAGO(2, "PAGO"),
    PREPARANDO_PARA_ENVIO(3, "PREPARANDO PARA ENVIO"),
    AGUARDANDO_ENTREGA(4, "AGUARDANDO ENTREGA"),
    ENTREGUE(5, "ENTREGUE");

    private final int codigo;
    private final String descricao;

    StatusPedido(int codigo, String nome) {
        this.codigo = codigo;
        this.descricao = nome;
    }
}
