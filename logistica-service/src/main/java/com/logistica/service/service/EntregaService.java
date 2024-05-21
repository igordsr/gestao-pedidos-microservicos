package com.logistica.service.service;

import com.logistica.service.dto.EntregaDTO;
import com.logistica.service.infrastructure.feign.ClienteServiceClient;
import com.logistica.service.infrastructure.feign.Pedido;
import com.logistica.service.infrastructure.feign.PedidoServiceClient;
import com.logistica.service.model.Entrega;
import com.logistica.service.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.main.java.com.logistica.service.infrastructure.feign.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EntregaService {
    private final EntregaRepository entregaRepository;
    private final ClienteServiceClient clienteServiceClient;
    private final PedidoServiceClient pedidoServiceClient;


    @Autowired
    public EntregaService(EntregaRepository entregaRepository, ClienteServiceClient clienteServiceClient, PedidoServiceClient pedidoServiceClient) {
        this.entregaRepository = entregaRepository;
        this.clienteServiceClient = clienteServiceClient;
        this.pedidoServiceClient = pedidoServiceClient;
    }

    public void atualizarEntrega(UUID idEntrega) {
        final Entrega entrega = this.entregaRepository.findById(idEntrega).orElseThrow();
        entrega.setDataEntrega(LocalDateTime.now());
        entregaRepository.save(entrega);
        pedidoServiceClient.confirmarEntrega(idEntrega);
    }

    public Map<String, List<EntregaDTO>> processarPedidosPagosEAgruparPorCep() {
        List<Pedido> listaPedidosPagos = pedidoServiceClient.getRelatorioPedidosPagos();
        Map<String, List<EntregaDTO>> entregasAgrupadasPorCep = new HashMap<>();

        for (Pedido pedido : listaPedidosPagos) {

            pedidoServiceClient.confirmarTransporte(pedido.identificador());
            Cliente cliente = clienteServiceClient.getClienteById(pedido.cliente());
            String cepPrefixo = cliente.cep().substring(0, 5);


            EntregaDTO entregaDTO = new EntregaDTO(
                    UUID.randomUUID(),
                    pedido.identificador(),
                    cliente.cep(),
                    isCepDeSaoPaulo(cliente.cep()) ? LocalDate.now().plusDays(3) : LocalDate.now().plusDays(10),
                    null, // Data de entrega
                    "EM_TRANSITO" // Status entrega
            );

            Entrega save = entregaRepository.save(entregaDTO.toEntrega());
            EntregaDTO instance = EntregaDTO.getInstance(save);

            entregasAgrupadasPorCep
                    .computeIfAbsent(cepPrefixo, k -> new ArrayList<>())
                    .add(instance);
        }

        return entregasAgrupadasPorCep;
    }

    public static boolean isCepDeSaoPaulo(String cep) {
        if (cep == null || cep.length() != 9 || !cep.matches("\\d{5}-\\d{3}")) {
            throw new IllegalArgumentException("CEP deve estar no formato 00000-000");
        }
        String cepPrefixo = cep.substring(0, 5);
        int prefix = Integer.parseInt(cepPrefixo);
        return (prefix >= 1000 && prefix <= 1999) || (prefix >= 60000 && prefix <= 199999);
    }


}
