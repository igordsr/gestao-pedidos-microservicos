package com.pedidos.service.domain.contract.rule.persistence;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IManterDados<T> {

    @Transactional
    T cadastrar(T objeto);

    @Transactional
    T atualizar(UUID identificador, T objeto);

    @Transactional
    List<T> atualizar(List<T> objeto);

    @Transactional
    void deletar(T objeto);
}
