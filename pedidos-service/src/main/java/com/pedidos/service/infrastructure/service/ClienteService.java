package com.pedidos.service.infrastructure.service;

import com.pedidos.service.domain.contract.IClienteContract;
import com.pedidos.service.infrastructure.feign.Cliente;
import com.pedidos.service.infrastructure.feign.ClienteServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class ClienteService implements IClienteContract {
    private final ClienteServiceClient clienteServiceClient;

    @Autowired
    public ClienteService(ClienteServiceClient clienteServiceClient) {
        this.clienteServiceClient = clienteServiceClient;
    }

    @Override
    public void verificarExistencia(UUID id) {
        Cliente cliente = this.clienteServiceClient.getClienteById(id);
        if (Objects.isNull(cliente)) {
            throw new RuntimeException("Teste");
        }
    }
}
