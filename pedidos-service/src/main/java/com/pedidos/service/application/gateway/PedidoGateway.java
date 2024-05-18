package com.pedidos.service.application.gateway;

import com.pedidos.service.domain.contract.IClienteContract;
import com.pedidos.service.domain.contract.IManderDadosPedidoContract;
import com.pedidos.service.domain.contract.IProdutoContract;
import com.pedidos.service.domain.dto.PedidoDTO;
import com.pedidos.service.domain.exception.CustomException;
import com.pedidos.service.domain.exception.RegistroNaoEncontradoException;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.usecase.PedidoUseCaseContract;

import java.util.List;
import java.util.UUID;

public class PedidoGateway {
    private final PedidoUseCaseContract pedidoUseCaseContract;

    public PedidoGateway(IClienteContract manterCliente, IProdutoContract materProduto, IManderDadosPedidoContract manterPedido) {
        this.pedidoUseCaseContract = new PedidoUseCaseContract(manterCliente, materProduto, manterPedido);
    }

    public PedidoDTO consultarPeloIdentificador(UUID identificador) throws RegistroNaoEncontradoException {
        return this.pedidoUseCaseContract.consultarPeloIdentificador(identificador).toDTO();
    }

    public PedidoDTO cadastrar(final PedidoDTO pedidoDTO) {
        Pedido pedido = this.pedidoUseCaseContract.cadastrar(pedidoDTO.toModal());
        return pedido.toDTO();
    }

    public List<PedidoDTO> gerarRelatorioPedidosPagos() throws CustomException {
        return this.pedidoUseCaseContract.gerarRelatorioPedidosPagos().stream().map(Pedido::toDTO).toList();
    }

    public PedidoDTO liquidarPedido(UUID identificador) throws CustomException {
        Pedido pedido = this.pedidoUseCaseContract.liquidarPedido(identificador);
        return pedido.toDTO();
    }

    public PedidoDTO enviar(UUID identificador) throws CustomException {
        Pedido pedido = this.pedidoUseCaseContract.enviar(identificador);
        return pedido.toDTO();
    }

    public PedidoDTO entregar(UUID identificador) throws CustomException {
        Pedido pedido = this.pedidoUseCaseContract.entregar(identificador);
        return pedido.toDTO();
    }
}
