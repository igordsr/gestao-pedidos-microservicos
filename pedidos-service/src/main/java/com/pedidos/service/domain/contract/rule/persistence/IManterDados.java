package com.pedidos.service.domain.contract.rule.persistence;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IManterDados<T> {

    @Transactional
    T cadastrar(T objeto);

    @Transactional
    T atualizar(T objeto);

    @Transactional
    List<T> atualizar(List<T> objeto);

}
