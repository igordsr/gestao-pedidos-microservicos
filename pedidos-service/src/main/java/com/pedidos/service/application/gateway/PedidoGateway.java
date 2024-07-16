package com.pedidos.service.application.gateway;

import com.pedidos.service.domain.contract.IClienteContract;
import com.pedidos.service.domain.contract.IManderDadosPedidoContract;
import com.pedidos.service.domain.contract.IProdutoContract;
import com.pedidos.service.domain.dto.PedidoDTO;
import com.pedidos.service.domain.dto.RelatorioDTO;
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


    public PedidoDTO atualizarPedido(UUID identificador, PedidoDTO pedidoDTOAtualizado) {
    Pedido pedido = this.pedidoUseCase.consultarPeloIdentificador(identificador);
    if (pedido == null) {
        throw new RegistroNaoEncontradoException("Pedido não encontrado.");
    }
        
        if (pedidoDTOAtualizado.itemList() == null || pedidoDTOAtualizado.itemList().isEmpty()) {
            this.pedidoUseCase.deletarPedido(pedido);
            return null; 
        } else {
            Pedido pedidoAtualizado = pedidoUseCase.atualizarPedido(pedido.getIdentificador(), pedidoDTOAtualizado.toModal());
            return pedidoAtualizado.toDTO();
        }
}

    public List<RelatorioDTO> gerarRelatorioPedidosPagos() {
        List<PedidoDTO> list = this.pedidoUseCase.gerarRelatorioPedidosPagos().stream().map(Pedido::toDTO).toList();
        return list.stream().map(item -> new RelatorioDTO(item.identificador(), item.cliente(), item.itemList())).toList();
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

    public List<PedidoDTO> consultarPeloIdClienteByToken() {
        List<Pedido> pedidos = this.pedidoUseCase.consultarPeloIdClienteByToken();
        return pedidos.stream().map(Pedido::toDTO).toList();
    }
}
