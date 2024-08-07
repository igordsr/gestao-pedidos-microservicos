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

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public final class PedidoUseCase implements IPedidoContract {
    private final IClienteContract manterCliente;
    private final IProdutoContract materProduto;
    private final IManderDadosPedidoContract manterPedido;

    public PedidoUseCase(IClienteContract manterCliente, IProdutoContract materProduto, IManderDadosPedidoContract manterPedido) {
        this.manterCliente = manterCliente;
        this.materProduto = materProduto;
        this.manterPedido = manterPedido;
    }

    @Override
    public Pedido consultarPeloIdentificador(UUID identificador) throws RegistroNaoEncontradoException {
        return this.manterPedido.consultarPeloIdentificador(identificador);
    }


    public Pedido atualizarPedido(UUID identificador, Pedido pedidoAtualizado) throws RegistroNaoEncontradoException {
        // Criar um mapa para armazenar os itens do pedido existente por produto
        Map<UUID, Item> itemMap = new HashMap<>();
        for (Item item : pedidoAtualizado.getItemList()) {
            UUID produto = item.getProduto();
            int quantidade = item.getQuantidade();

            // Verificar se o produto já existe no mapa
            if (itemMap.containsKey(produto)) {
                // Se existir, somar a quantidade
                Item itemExistente = itemMap.get(produto);
                itemExistente = new Item(produto, itemExistente.getQuantidade() + quantidade);
                itemMap.put(produto, itemExistente);
            } else {
                // Se não existir, adicionar o item ao mapa
                itemMap.put(produto, new Item(produto, quantidade));
            }
        }

        // Criar uma lista com os itens atualizados
        List<Item> itensAtualizados = new ArrayList<>(itemMap.values());

        // Criar um novo pedido com a lista de itens atualizada
        Pedido pedidoAtualizadoComItens = new Pedido(
                pedidoAtualizado.getIdentificador(),
                pedidoAtualizado.getCliente(),
                itensAtualizados,
                pedidoAtualizado.getStatusPedido()
        );

        // Atualizar o pedido no sistema de manutenção de pedidos
        return this.manterPedido.atualizar(identificador, pedidoAtualizadoComItens);
    }

    @Override
    public void deletarPedido(Pedido pedido) throws RegistroNaoEncontradoException {
        this.manterPedido.deletar(pedido);
    }

    @Override
    public List<Pedido> consultarPeloIdClienteByToken() {
        return this.manterPedido.consultarPeloIdClienteByToken();
    }

    @Override
    public List<Pedido> listarPedidos() {
        return this.manterPedido.listarPedidos();
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
        if (!pedido.getStatusPedido().equals(StatusPedido.AGUARDANDO_PAGAMENTO)) {
            final String errorMsg = "Pedido já foi pago anteriormente e está no status de %s";
            throw new EntidadeNaoProcessavelException(format(errorMsg, pedido.getStatusPedido().getDescricao()));
        }
        for (Item item : pedido.getItemList()) {
            this.materProduto.diminuirQuantidadeProdutoEstoque(item.getProduto(), item.getQuantidade());
        }
        pedido.setStatusPedido(StatusPedido.PAGO);
        return this.manterPedido.atualizar(identificador, pedido);
    }

    @Override
    public List<Pedido> gerarRelatorioPedidosPagos() {
        final List<Pedido> pedidos = this.manterPedido.consultarPeloStatus(List.of(StatusPedido.PAGO, StatusPedido.PREPARANDO_PARA_ENVIO));
        pedidos.forEach(pedido -> pedido.setStatusPedido(StatusPedido.PREPARANDO_PARA_ENVIO));
        return this.manterPedido.atualizar(pedidos);
    }

    @Override
    public Pedido enviar(UUID identificador) throws RegistroNaoEncontradoException {
        final Pedido pedido = this.manterPedido.consultarPeloIdentificador(identificador);
        if (pedido.getStatusPedido().equals(StatusPedido.AGUARDANDO_PAGAMENTO)) {
            final String errorMsg = "Este pedido não pode ser liberado para transporte pois está pendente de pagamento.";
            throw new EntidadeNaoProcessavelException(errorMsg);
        }
        pedido.setStatusPedido(StatusPedido.AGUARDANDO_ENTREGA);
        return this.manterPedido.atualizar(identificador, pedido);
    }

    @Override
    public Pedido entregar(UUID identificador) throws CustomException {
        final Pedido pedido = this.manterPedido.consultarPeloIdentificador(identificador);
        if (pedido.getStatusPedido().equals(StatusPedido.AGUARDANDO_PAGAMENTO)) {
            final String errorMsg = "Este pedido não pode ser liberado para entrega pois está pendente de pagamento.";
            throw new EntidadeNaoProcessavelException(errorMsg);
        }
        pedido.setStatusPedido(StatusPedido.ENTREGUE);
        return this.manterPedido.atualizar(identificador, pedido);
    }

    private void verificarDisponibilidadeDoProduto(final Pedido pedido, final List<Item> items) {
        processarItens(pedido, items);
    }


    private void processarItens(final Pedido pedido, final List<Item> produtos) throws EntidadeNaoProcessavelException {
        for (final Item itemRequerido : pedido.getItemList()) {
            final Item produto = getItemFromList(itemRequerido, produtos);
            if (produto.getQuantidade() < itemRequerido.getQuantidade()) {
                final String errorMsg = "Não há quantidade suficiente disponível do  produto [%s]. Por favor, ajuste a quantidade ou escolha outro produto";
                throw new EntidadeNaoProcessavelException(format(errorMsg, produto.getProduto().toString()));
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
