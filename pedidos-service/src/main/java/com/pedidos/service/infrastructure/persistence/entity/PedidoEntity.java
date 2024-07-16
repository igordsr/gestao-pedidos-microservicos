package com.pedidos.service.infrastructure.persistence.entity;

import com.pedidos.service.domain.contract.rule.application.IConverterToModal;
import com.pedidos.service.domain.model.Item;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.model.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "pedidos")
public class PedidoEntity implements IConverterToModal<Pedido> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, name = "id_cliente")
    private UUID idCliente;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemEntity> itens;
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private StatusPedido statusPedido;

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    @Override
    public Pedido toModal() {
        final List<Item> itens = this.itens.stream().map(ItemEntity::toModal).toList();
        return new Pedido(this.id, this.idCliente, itens, this.statusPedido);
    }

    public static PedidoEntity getInstance(final Pedido objeto) {
        final PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.idCliente = objeto.getCliente();
        pedidoEntity.statusPedido = objeto.getStatusPedido();
        pedidoEntity.itens = ItemEntity.getInstance(pedidoEntity, objeto.getItemList());
        return pedidoEntity;
    }

    public void setItens(List<ItemEntity> itens) {
        this.itens = itens;
    }
}
