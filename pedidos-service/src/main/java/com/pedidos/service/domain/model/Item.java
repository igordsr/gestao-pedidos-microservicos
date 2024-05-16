package com.pedidos.service.domain.model;

import com.pedidos.service.domain.contract.rule.application.IConverterToDTO;
import com.pedidos.service.domain.dto.ItemDTO;

import java.util.UUID;

public class Item implements IConverterToDTO<ItemDTO> {
    private final UUID produto;
    private final Integer quantidade;

    public Item(UUID produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public UUID getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    @Override
    public ItemDTO toDTO() {
        return new ItemDTO(this.produto, this.quantidade);
    }
}
