package com.pedidos.service.infrastructure.service;

import com.pedidos.service.domain.contract.IClienteContract;
import com.pedidos.service.infrastructure.feign.UsuarioServiceClient;
import com.pedidos.service.infrastructure.feign.vo.UsuarioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class ClienteService implements IClienteContract {
    private final UsuarioServiceClient usuarioServiceClient;

    @Autowired
    public ClienteService(UsuarioServiceClient usuarioServiceClient) {
        this.usuarioServiceClient = usuarioServiceClient;
    }

    @Override
    public void verificarExistencia(UUID id) {
        UsuarioVO cliente = this.usuarioServiceClient.getUsuarioById(id);
        if (Objects.isNull(cliente)) {
            throw new RuntimeException("Teste");
        }
    }
}
