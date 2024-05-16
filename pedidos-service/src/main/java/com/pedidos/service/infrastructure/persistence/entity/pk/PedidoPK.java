package com.pedidos.service.infrastructure.persistence.entity.pk;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class PedidoPK implements Serializable {
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "id_cliente")
    private UUID idCliente;

}