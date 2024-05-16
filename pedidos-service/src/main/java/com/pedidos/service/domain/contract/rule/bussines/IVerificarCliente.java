package com.pedidos.service.domain.contract.rule.bussines;

import java.util.UUID;

public interface IVerificarCliente {
    void verificarExistencia(UUID cliente);
}
