package com.pedidos.service.domain.model;

import com.pedidos.service.domain.contract.rule.application.IConverterToDTO;
import com.pedidos.service.domain.dto.PedidoDTO;

import java.util.List;
import java.util.UUID;

public class Pedido implements IConverterToDTO<PedidoDTO> {
    private UUID identificador;
    private final UUID cliente;
    private final List<Item> itemList;
    private StatusPedido statusPedido;

    public Pedido(UUID cliente, List<Item> itemList) {
        this.cliente = cliente;
        this.itemList = itemList;
        this.statusPedido = StatusPedido.AGUARDANDO_PAGAMENTO;
    }

    public Pedido(UUID identificador, UUID cliente, List<Item> itemList, StatusPedido statusPedido) {
        this.identificador = identificador;
        this.cliente = cliente;
        this.itemList = itemList;
        this.statusPedido = statusPedido;
    }

    public UUID getIdentificador() {
        return identificador;
    }

    public UUID getCliente() {
        return cliente;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    @Override
    public PedidoDTO toDTO() {
        return new PedidoDTO(this.identificador, this.cliente, this.itemList.stream().map(Item::toDTO).toList(), this.statusPedido.getDescricao());
    }
}
