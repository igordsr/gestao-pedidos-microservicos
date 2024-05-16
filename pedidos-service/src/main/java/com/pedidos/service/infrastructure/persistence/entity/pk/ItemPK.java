package com.pedidos.service.infrastructure.persistence.entity.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
public class ItemPK implements Serializable {
    @Column(name = "id_produto", nullable = false)
    private UUID produtoId;

    @Column(name = "pedido_id", nullable = false)
    private UUID pedidoId;

    public ItemPK(UUID produtoId, UUID pedidoId) {
        this.produtoId = produtoId;
        this.pedidoId = pedidoId;
    }

    public UUID getProdutoId() {
        return produtoId;
    }
}