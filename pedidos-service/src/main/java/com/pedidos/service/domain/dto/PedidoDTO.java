package com.pedidos.service.domain.dto;

import com.pedidos.service.domain.contract.rule.application.IConverterToModal;
import com.pedidos.service.domain.model.Pedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PedidoDTO(
        UUID identificador,
        @Schema(example = "Camiseta de algodão com estampa")
        @NotNull(message = "Descrição do produto não pode estar em nulo.")
        UUID cliente,
        @NotNull(message = "Descrição do produto não pode estar em nulo.")
        @NotEmpty(message = "Descrição do produto não pode estar em nulo.")
        List<@Valid ItemDTO> itemList,
        String status
) implements IConverterToModal<Pedido> {
    @Override
    public Pedido toModal() {
        return new Pedido(cliente, itemList.stream().map(ItemDTO::toModal).toList());
    }
}
