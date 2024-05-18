package com.pedidos.service.util;

import com.pedidos.service.domain.dto.ItemDTO;
import com.pedidos.service.domain.dto.PedidoDTO;
import com.pedidos.service.domain.model.Item;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.model.StatusPedido;

import java.util.List;
import java.util.UUID;

public abstract class InstanceGeneratorHelper {

    public static Item getItem() {
        return new Item(UUID.fromString("232d0ce3-1ffd-4b22-91d6-ee6e45834167"), 100);
    }

    public static Pedido getPedido() {
        return new Pedido(
                UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed"),
                UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"),
                List.of(getItem()),
                StatusPedido.AGUARDANDO_PAGAMENTO
        );
    }

    public static ItemDTO getItemDTO() {
        final Item item = getItem();
        return new ItemDTO(item.getProduto(), item.getQuantidade());
    }

    public static PedidoDTO getPedidoDTO() {
        return new PedidoDTO(null, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(getItemDTO()), StatusPedido.AGUARDANDO_PAGAMENTO.getDescricao());
    }
}
