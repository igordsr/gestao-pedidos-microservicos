package com.pedidos.service.infrastructure.service;

import com.pedidos.service.domain.contract.IManderDadosPedidoContract;
import com.pedidos.service.domain.exception.RegistroNaoEncontradoException;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.model.StatusPedido;
import com.pedidos.service.infrastructure.config.SecurityUtils;
import com.pedidos.service.infrastructure.persistence.entity.ItemEntity;
import com.pedidos.service.infrastructure.persistence.entity.PedidoEntity;
import com.pedidos.service.infrastructure.persistence.entity.pk.ItemPK;
import com.pedidos.service.infrastructure.persistence.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoService implements IManderDadosPedidoContract {
    private final PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido consultarPeloIdentificador(UUID identificador) throws RegistroNaoEncontradoException {
        final PedidoEntity pedidoEntity = this.pedidoRepository.findById(identificador).orElseThrow(() -> new RegistroNaoEncontradoException(identificador.toString()));
        return pedidoEntity.toModal();
    }

    @Override
    public List<Pedido> consultarPeloStatus(StatusPedido statusPedido) throws RegistroNaoEncontradoException {
        final List<PedidoEntity> pedidos = this.pedidoRepository.findByStatusPedido(statusPedido);
        return this.mapTpListPedidos(pedidos);
    }

    @Override
    public List<Pedido> consultarPeloStatus(List<StatusPedido> statusPedido) throws RegistroNaoEncontradoException {
        final List<PedidoEntity> pedidos = this.pedidoRepository.findByStatusPedidoIn(statusPedido);
        return this.mapTpListPedidos(pedidos);
    }

    @Override
    public List<Pedido> consultarPeloIdClienteByToken() throws RegistroNaoEncontradoException {
        final List<PedidoEntity> pedidos = this.pedidoRepository.findByIdCliente(SecurityUtils.getCurrentUserUUID());
        return this.mapTpListPedidos(pedidos);
    }

    @Override
    public List<Pedido> listarPedidos() {
        return this.pedidoRepository.findAll().stream().map(PedidoEntity::toModal).toList();
    }

    @Override
    public Pedido cadastrar(Pedido objeto) {
        PedidoEntity pedidoEntity = PedidoEntity.getInstance(objeto);
        pedidoEntity = this.pedidoRepository.save(pedidoEntity);
        return pedidoEntity.toModal();
    }

    @Override
    public Pedido atualizar(UUID identificador, Pedido pedido) {
        final PedidoEntity pedidoEntity = this.pedidoRepository.findById(identificador).orElseThrow(() -> new RegistroNaoEncontradoException(identificador.toString()));
        final List<ItemEntity> itens = ItemEntity.getInstance(pedidoEntity, pedido.getItemList());
        pedidoEntity.setStatusPedido(pedido.getStatusPedido());
        this.mergeItens(pedidoEntity, itens);
        pedido = this.pedidoRepository.save(pedidoEntity).toModal();
        return pedido;
    }

    private void mergeItens(final PedidoEntity pedidoEntity, List<ItemEntity> itens) {
        // Map to hold the current items with their produtoId as key
        Map<UUID, ItemEntity> currentItemsMap = pedidoEntity.getItens().stream().collect(Collectors.toMap(item -> item.getId().getProdutoId(), item -> item));
        // List to hold new items
        List<ItemEntity> newItems = new ArrayList<>();

        // Process incoming items
        for (ItemEntity incomingItem : itens) {
            ItemPK incomingItemPK = incomingItem.getId();
            UUID produtoId = incomingItemPK.getProdutoId();

            if (currentItemsMap.containsKey(produtoId)) {
                // Update existing item
                ItemEntity existingItem = currentItemsMap.get(produtoId);
                existingItem.setQuantidade(incomingItem.getQuantidade());
                currentItemsMap.remove(produtoId);
            } else {
                // Add new item
                newItems.add(incomingItem);
            }
        }

        // Remaining items in currentItemsMap need to be removed
        List<ItemEntity> itemsToRemove = new ArrayList<>(currentItemsMap.values());

        // Update pedidoEntity's items
        pedidoEntity.getItens().removeAll(itemsToRemove);
        pedidoEntity.getItens().addAll(newItems);
    }

    @Override
    public void deletar(Pedido objeto) {
        this.pedidoRepository.delete(PedidoEntity.getInstance(objeto));
    }

    @Override
    public List<Pedido> atualizar(List<Pedido> objeto) {
        List<UUID> uuids = objeto.stream().map(Pedido::getIdentificador).toList();
        List<PedidoEntity> pedidoEntities = this.pedidoRepository.findAllById(uuids);
        this.atualizarStatusPedidos(objeto, pedidoEntities);
        pedidoEntities = this.pedidoRepository.saveAll(pedidoEntities);
        return this.mapTpListPedidos(pedidoEntities);
    }


    private List<Pedido> mapTpListPedidos(List<PedidoEntity> pedidoEntities) {
        return pedidoEntities.stream().map(PedidoEntity::toModal).toList();
    }

    public void atualizarStatusPedidos(List<Pedido> objetos, List<PedidoEntity> pedidoEntities) throws RegistroNaoEncontradoException {
        for (Pedido pedido : objetos) {
            boolean encontrado = false;
            for (PedidoEntity pedidoEntity : pedidoEntities) {
                if (pedidoEntity.getId().equals(pedido.getIdentificador())) {
                    pedidoEntity.setStatusPedido(pedido.getStatusPedido());
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                throw new RegistroNaoEncontradoException(pedido.getIdentificador().toString());
            }
        }
    }

}
