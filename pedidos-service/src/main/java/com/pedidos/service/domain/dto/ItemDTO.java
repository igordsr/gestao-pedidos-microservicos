package com.pedidos.service.domain.dto;

import com.pedidos.service.domain.contract.rule.application.IConverterToModal;
import com.pedidos.service.domain.model.Item;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemDTO(
        @NotNull(message = "Descrição do produto não pode estar em nulo.")
        UUID produto,
        @NotNull(message = "Descrição do produto não pode estar em nulo.")
        Integer quantidade) implements IConverterToModal<Item> {
    @Override
    public Item toModal() {
        return new Item(this.produto, this.quantidade);
    }
}
