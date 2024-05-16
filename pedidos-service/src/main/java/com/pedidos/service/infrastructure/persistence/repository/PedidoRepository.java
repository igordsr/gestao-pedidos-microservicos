package com.pedidos.service.infrastructure.persistence.repository;

import com.pedidos.service.domain.model.StatusPedido;
import com.pedidos.service.infrastructure.persistence.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, UUID> {
    List<PedidoEntity> findByStatusPedido(StatusPedido statusPedido);

}
