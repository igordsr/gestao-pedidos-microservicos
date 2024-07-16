package com.pedidos.service.infrastructure.service;

import com.pedidos.service.domain.contract.IManderDadosPedidoContract;
import com.pedidos.service.domain.exception.RegistroNaoEncontradoException;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.model.StatusPedido;
import com.pedidos.service.infrastructure.config.SecurityUtils;
import com.pedidos.service.infrastructure.persistence.entity.ItemEntity;
import com.pedidos.service.infrastructure.persistence.entity.PedidoEntity;
import com.pedidos.service.infrastructure.persistence.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
    public Pedido atualizar(UUID identificador, Pedido objeto) {
        final PedidoEntity pedidoEntity = this.pedidoRepository.findById(identificador).orElseThrow(() -> new RegistroNaoEncontradoException(objeto.getIdentificador().toString()));
        final List<ItemEntity> itens = ItemEntity.getInstance(pedidoEntity, objeto.getItemList());
        pedidoEntity.setStatusPedido(objeto.getStatusPedido());
        pedidoEntity.setItens(itens);
        return this.pedidoRepository.save(pedidoEntity).toModal();
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
