package com.pedidos.service.application.gateway;

import com.pedidos.service.domain.contract.IClienteContract;
import com.pedidos.service.domain.contract.IManderDadosPedidoContract;
import com.pedidos.service.domain.contract.IProdutoContract;
import com.pedidos.service.domain.dto.PedidoDTO;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.usecase.PedidoUseCase;

import java.util.List;
import java.util.UUID;

public class PedidoGateway {
    private final PedidoUseCase pedidoUseCase;

    public PedidoGateway(IClienteContract manterCliente, IProdutoContract materProduto, IManderDadosPedidoContract manterPedido) {
        this.pedidoUseCase = new PedidoUseCase(manterCliente, materProduto, manterPedido);
    }

    public PedidoDTO consultarPeloIdentificador(UUID identificador) {
        return this.pedidoUseCase.consultarPeloIdentificador(identificador).toDTO();
    }

    public PedidoDTO cadastrar(PedidoDTO pedidoDTO) {
        final Pedido pedido = this.pedidoUseCase.cadastrar(pedidoDTO.toModal());
        pedidoDTO = pedido.toDTO();
        return pedidoDTO;
    }

    public List<PedidoDTO> gerarRelatorioPedidosPagos() {
        return this.pedidoUseCase.gerarRelatorioPedidosPagos().stream().map(Pedido::toDTO).toList();
    }

    public PedidoDTO liquidarPedido(UUID identificador) {
        Pedido pedido = this.pedidoUseCase.liquidarPedido(identificador);
        return pedido.toDTO();
    }

    public PedidoDTO enviar(UUID identificador) {
        Pedido pedido = this.pedidoUseCase.enviar(identificador);
        return pedido.toDTO();
    }

    public PedidoDTO entregar(UUID identificador) {
        Pedido pedido = this.pedidoUseCase.entregar(identificador);
        return pedido.toDTO();
    }
}
