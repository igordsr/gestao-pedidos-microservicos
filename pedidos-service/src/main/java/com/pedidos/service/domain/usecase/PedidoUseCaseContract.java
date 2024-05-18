package com.pedidos.service.domain.usecase;

import com.pedidos.service.domain.contract.IClienteContract;
import com.pedidos.service.domain.contract.IManderDadosPedidoContract;
import com.pedidos.service.domain.contract.IPedidoContract;
import com.pedidos.service.domain.contract.IProdutoContract;
import com.pedidos.service.domain.exception.CustomException;
import com.pedidos.service.domain.exception.EntidadeNaoProcessavelException;
import com.pedidos.service.domain.exception.RegistroNaoEncontradoException;
import com.pedidos.service.domain.model.Item;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.model.StatusPedido;

import java.util.List;
import java.util.UUID;

public final class PedidoUseCaseContract implements IPedidoContract {
    private final IClienteContract manterCliente;
    private final IProdutoContract materProduto;
    private final IManderDadosPedidoContract manterPedido;

    public PedidoUseCaseContract(IClienteContract manterCliente, IProdutoContract materProduto, IManderDadosPedidoContract manterPedido) {
        this.manterCliente = manterCliente;
        this.materProduto = materProduto;
        this.manterPedido = manterPedido;
    }

    @Override
    public Pedido consultarPeloIdentificador(UUID identificador) throws RegistroNaoEncontradoException {
        return this.manterPedido.consultarPeloIdentificador(identificador);
    }

    @Override
    public Pedido cadastrar(Pedido pedido) throws CustomException {
        this.manterCliente.verificarExistencia(pedido.getCliente());
        final List<Item> produtos = this.materProduto.consultarProdutos(pedido.getItemList().stream().map(Item::getProduto).toList());
        this.verificarDisponibilidadeDoProduto(pedido, produtos);
        return this.manterPedido.cadastrar(pedido);
    }

    @Override
    public Pedido liquidarPedido(UUID identificador) {
        final Pedido pedido = this.manterPedido.consultarPeloIdentificador(identificador);
        pedido.setStatusPedido(StatusPedido.PAGO);
        return this.manterPedido.atualizar(pedido);
    }

    @Override
    public List<Pedido> gerarRelatorioPedidosPagos() {
        final List<Pedido> pedidos = this.manterPedido.consultarPeloStatus(StatusPedido.PAGO);
        pedidos.forEach(pedido -> pedido.setStatusPedido(StatusPedido.PREPARANDO_PARA_ENVIO));
        return this.manterPedido.atualizar(pedidos);
    }

    @Override
    public Pedido enviar(UUID identificador) throws RegistroNaoEncontradoException {
        final Pedido pedido = this.manterPedido.consultarPeloIdentificador(identificador);
        pedido.setStatusPedido(StatusPedido.AGUARDANDO_ENTREGA);
        return this.manterPedido.atualizar(pedido);
    }

    @Override
    public Pedido entregar(UUID identificador) throws CustomException {
        final Pedido pedido = this.manterPedido.consultarPeloIdentificador(identificador);
        pedido.setStatusPedido(StatusPedido.ENTREGUE);
        return this.manterPedido.atualizar(pedido);
    }

    private void verificarDisponibilidadeDoProduto(final Pedido pedido, final List<Item> items) {
        processarItens(pedido, items);
    }


    private void processarItens(final Pedido pedido, final List<Item> produtos) throws EntidadeNaoProcessavelException {
        for (final Item itemRequerido : pedido.getItemList()) {
            final Item produto = getItemFromList(itemRequerido, produtos);
            if (produto.getQuantidade() < itemRequerido.getQuantidade()) {
                throw new EntidadeNaoProcessavelException(produto.getProduto().toString());
            }
        }
    }

    private static Item getItemFromList(final Item item, final List<Item> produtos) throws RegistroNaoEncontradoException {
        for (Item produto : produtos) {
            if (produto.getProduto().equals(item.getProduto())) {
                return produto;
            }
        }
        throw new RegistroNaoEncontradoException(item.getProduto().toString());
    }

}
