package com.pedidos.service.infrastructure.persistence.entity;

import com.pedidos.service.domain.contract.rule.application.IConverterToModal;
import com.pedidos.service.domain.model.Item;
import com.pedidos.service.infrastructure.persistence.entity.pk.ItemPK;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itens_pedidos")
public class ItemEntity implements IConverterToModal<Item> {
    @EmbeddedId
    private ItemPK id;
    @ManyToOne
    @MapsId("pedidoId")
    @JoinColumn(name = "pedido_id")
    private PedidoEntity pedido;
    private Integer quantidade;

    @Override
    public Item toModal() {
        return new Item(this.id.getProdutoId(), this.quantidade);
    }

    public static List<ItemEntity> getInstance(final PedidoEntity pedidoEntity, List<Item> itemList) {
        final List<ItemEntity> itemEntities = new ArrayList<>();
        for (Item item : itemList) {
            final ItemEntity itemEntity = new ItemEntity();
            itemEntity.id = new ItemPK(item.getProduto(), pedidoEntity.getId());
            itemEntity.pedido = pedidoEntity;
            itemEntity.quantidade = item.getQuantidade();
            itemEntities.add(itemEntity);
        }
        return itemEntities;
    }
}